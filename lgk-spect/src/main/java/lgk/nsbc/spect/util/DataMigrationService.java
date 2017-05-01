package lgk.nsbc.spect.util;

import com.vaadin.spring.annotation.VaadinSessionScope;
import lgk.nsbc.model.dao.*;
import lgk.nsbc.model.*;
import lgk.nsbc.spect.model.StudyRecords;
import lgk.nsbc.spect.model.StudyTarget;
import lgk.nsbc.spect.model.Target;
import lgk.nsbc.spect.util.excel.ParserService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

@Service
@VaadinSessionScope
public class DataMigrationService implements Serializable{
    @Autowired
    private ParserService parserService;
    @Autowired
    private BasPeopleDao basPeopleDao;
    @Autowired
    private NbcStudDao nbcStudDao;
    @Autowired
    private NbcFollowUpDao nbcFollowUpDao;
    @Autowired
    private NbcFlupSpectDataDao nbcFlupSpectDataDao;
    @Autowired
    private NbcStudInjDao nbcStudInjDao;
    @Autowired
    private PatientsDuplicatesResolver duplicatesResolver;

    public void findPatients(File tempFile) {
        try {
            // Получаем записи
            Map<String, TreeSet<StudyRecords>> records = mapByFullName(tempFile);
            // Пытаемся найти паицентов, присутствующих в записях.
            StringBuilder shortMessage = new StringBuilder();
            Map<String, Optional<NbcPatients>> patients = parseNames(records.keySet(), shortMessage);
            persist(records, patients);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Должен проверить, что такой поцедуры нет.
     * Сохраняет процедуры у пациентов.
     */
    public void persist(Map<String, TreeSet<StudyRecords>> patientRecords,
                        Map<String, Optional<NbcPatients>> patients) {
        for (Map.Entry<String, Optional<NbcPatients>> patientEntry : patients.entrySet()) {
            if (!patientEntry.getValue().isPresent()) continue;
            NbcPatients patient = patientEntry.getValue().get();
            TreeSet<StudyRecords> studyRecordsTreeSet = patientRecords.get(patientEntry.getKey());
            for (StudyRecords records : studyRecordsTreeSet) {
                NbcStud nbcStud = studyFromRecord(records, patient);
                // Запись о исследовании
                if (!nbcStudDao.isSpectStudyExist(nbcStud.getNbc_patients_n(), nbcStud.getStudydatetime())) {
                    nbcStudDao.createNbcStud(nbcStud);
                }
                Optional<NbcStudInj> injOptional = nbcStudInjDao.findByStudy(nbcStud);
                if (injOptional.isPresent()) {
                    NbcStudInj nbcStudInj = injOptional.get();
                    nbcStudInj.setInj_activity_bq(records.getDose());
                    nbcStudInjDao.updateInj(nbcStudInj);
                } else {
                    NbcStudInj nbcStudInj = NbcStudInj.builder()
                            .nbc_stud_n(nbcStud.getN())
                            .inj_activity_bq(records.getDose())
                            .build();
                    nbcStudInjDao.insertStudInj(nbcStudInj);
                }
                // Это все разные мишени, - разные записи в NbcFollowUp
                List<StudyTarget> targets = records.getTargets();
                List<NbcFollowUp> followUps = nbcFollowUpDao.findByStudy(nbcStud);
                // Заносим в базу, только если данных нет
                if (followUps.isEmpty()) {
                    for (StudyTarget target : targets) {
                        NbcFollowUp nbcFollowUp = NbcFollowUp.builder()
                                .nbc_stud_n(nbcStud.getN())
                                .build();
                        List<NbcFlupSpectData> dataList = target.getTargets()
                                .stream()
                                .map(this::fromTarget)
                                .collect(toList());
                        nbcFlupSpectDataDao.createSpectFollowUpData(nbcFollowUp, dataList);
                    }
                }
            }
        }
    }

    /**
     * Не забываем что nbc followup n будет известно только после сохранения в бд
     *
     * @param target
     * @return
     */
    private NbcFlupSpectData fromTarget(Target target) {
        return NbcFlupSpectData.builder()
                .contourType(target.getContourType())
                .targetType(target.getTargetType())
                .volume(target.getVolume())
                .early_phase(target.getCountsAfter30min())
                .late_phase(target.getCountsAfter60min())
                .build();
    }


    private NbcStud studyFromRecord(StudyRecords studyRecords, NbcPatients patient) {
        return NbcStud.builder()
                .nbc_patients_n(patient.getN())
                .study_type(11L)
                .studydatetime(studyRecords.getDate())
                .build();
    }

    private Map<String, Optional<NbcPatients>> parseNames(Set<String> names, StringBuilder shortMessage) {
        Map<String, Optional<NbcPatients>> peoples = new TreeMap<>();
        Set<String> surnames = names.stream()
                .map(name -> name.trim().split(" ")[0])
                .collect(toSet());
        List<BasPeople> basPeoples = basPeopleDao.getPeoplesBySurname(surnames);
        Map<String[], BasPeople> basPeopleMap = basPeoples.stream()
                .collect(toMap(people -> new String[]{people.getSurname(), people.getName(), people.getPatronymic()}, identity()));
        for (String fullName : names) {
            String[] parsedName = fullName.trim().split(" ");
            if (parsedName.length == 3) {
                // Нашли всех возможных дубликатов
                List<BasPeople> man = basPeopleMap.entrySet().stream()
                        .filter(entry -> Arrays.deepEquals(entry.getKey(), parsedName))
                        .map(Map.Entry::getValue)
                        .collect(toList());
                if (man.isEmpty()) {
                    peoples.put(fullName, Optional.empty());
                    shortMessage.append(fullName)
                            .append("Не нашелся в базе\n");
                } else {
                    // Находим нужного паицента
                    Optional<NbcPatients> patient = duplicatesResolver.getPatient(man.get(0));
                    peoples.put(fullName, patient);
                }
            } else {
                peoples.put(fullName, Optional.empty());
                shortMessage.append(fullName)
                        .append("Имя не распозналось из Excel\n");
            }
        }
        return peoples;
    }

    private Map<String, TreeSet<StudyRecords>> mapByFullName(File tempFile) throws IOException, InvalidFormatException {
        XSSFWorkbook workbook = new XSSFWorkbook(tempFile);
        XSSFSheet sheetAt = workbook.getSheetAt(0);
        List<StudyRecords> studyRecords = parserService.parseSheet(sheetAt);
        Map<String, TreeSet<StudyRecords>> map = new TreeMap<>();
        for (StudyRecords record : studyRecords) {
            map.computeIfAbsent(record.getFullName(), s -> new TreeSet<>((o1, o2) -> o1.getDate().compareTo(o2.getDate())));
            map.get(record.getFullName()).add(record);
        }
        return map;
    }
}
