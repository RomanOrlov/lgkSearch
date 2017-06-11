package lgk.nsbc.spect.util;

import com.vaadin.spring.annotation.VaadinSessionScope;
import lgk.nsbc.model.dao.*;
import lgk.nsbc.model.*;
import lgk.nsbc.model.dao.dictionary.StudTypeDao;
import lgk.nsbc.spect.model.StudyRecords;
import lgk.nsbc.spect.model.StudyTarget;
import lgk.nsbc.spect.model.Target;
import lgk.nsbc.spect.util.excel.ParserService;
import lombok.RequiredArgsConstructor;
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
//@VaadinSessionScope
@RequiredArgsConstructor
public class DataMigrationService implements Serializable{
    private final ParserService parserService;
    private final PeopleDao peopleDao;
    private final StudDao studDao;
    private final FollowUpDao followUpDao;
    private final FlupSpectDataDao flupSpectDataDao;
    private final StudInjDao studInjDao;
    private final PatientsDuplicatesResolver duplicatesResolver;

    public void findPatients(File tempFile) {
        try {
            // Получаем записи
            Map<String, TreeSet<StudyRecords>> records = mapByFullName(tempFile);
            // Пытаемся найти паицентов, присутствующих в записях.
            StringBuilder shortMessage = new StringBuilder();
            Map<String, Optional<Patients>> patients = parseNames(records.keySet(), shortMessage);
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
                        Map<String, Optional<Patients>> patients) {
        for (Map.Entry<String, Optional<Patients>> patientEntry : patients.entrySet()) {
            if (!patientEntry.getValue().isPresent()) continue;
            Patients patient = patientEntry.getValue().get();
            TreeSet<StudyRecords> studyRecordsTreeSet = patientRecords.get(patientEntry.getKey());
            for (StudyRecords records : studyRecordsTreeSet) {
                Stud stud = studyFromRecord(records, patient);
                // Запись о исследовании
                if (!studDao.isSpectStudyExist(stud.getPatientsN(), stud.getStudyDateTime())) {
                    studDao.createStud(stud);
                }
                Optional<StudInj> injOptional = studInjDao.findByStudy(stud);
                if (injOptional.isPresent()) {
                    StudInj studInj = injOptional.get();
                    studInj.setInjActivityBq(records.getDose());
                    studInjDao.updateInj(studInj);
                } else {
                    StudInj studInj = StudInj.builder()
                            .studN(stud.getN())
                            .injActivityBq(records.getDose())
                            .build();
                    studInjDao.insertStudInj(studInj);
                }
                // Это все разные мишени, - разные записи в NbcFollowUp
                List<StudyTarget> targets = records.getTargets();
                List<FollowUp> followUps = followUpDao.findByStudy(stud);
                // Заносим в базу, только если данных нет
                if (followUps.isEmpty()) {
                    for (StudyTarget target : targets) {
                        FollowUp followUp = FollowUp.builder()
                                .studN(stud.getN())
                                .build();
                        List<FlupSpectData> dataList = target.getTargets()
                                .stream()
                                .map(this::fromTarget)
                                .collect(toList());
                        flupSpectDataDao.createSpectFollowUpData(followUp, dataList);
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
    private FlupSpectData fromTarget(Target target) {
        return FlupSpectData.builder()
                .contourType(target.getContourType())
                .targetType(target.getTargetType())
                .volume(target.getVolume())
                .earlyPhase(target.getCountsAfter30min())
                .latePhase(target.getCountsAfter60min())
                .build();
    }


    private Stud studyFromRecord(StudyRecords studyRecords, Patients patient) {
        return Stud.builder()
                .patientsN(patient.getN())
                .studType(StudTypeDao.getStudTypeMap().get(11L))
                .studyDateTime(studyRecords.getDate())
                .build();
    }

    private Map<String, Optional<Patients>> parseNames(Set<String> names, StringBuilder shortMessage) {
        Map<String, Optional<Patients>> optionalPeoples = new TreeMap<>();
        Set<String> surnames = names.stream()
                .map(name -> name.trim().split(" ")[0])
                .collect(toSet());
        List<People> peoples = peopleDao.getPeoplesBySurname(surnames);
        Map<String[], People> basPeopleMap = peoples.stream()
                .collect(toMap(people -> new String[]{people.getSurname(), people.getName(), people.getPatronymic()}, identity()));
        for (String fullName : names) {
            String[] parsedName = fullName.trim().split(" ");
            if (parsedName.length == 3) {
                // Нашли всех возможных дубликатов
                List<People> man = basPeopleMap.entrySet().stream()
                        .filter(entry -> Arrays.deepEquals(entry.getKey(), parsedName))
                        .map(Map.Entry::getValue)
                        .collect(toList());
                if (man.isEmpty()) {
                    optionalPeoples.put(fullName, Optional.empty());
                    shortMessage.append(fullName)
                            .append("Не нашелся в базе\n");
                } else {
                    // Находим нужного паицента
                    Optional<Patients> patient = duplicatesResolver.getPatient(man.get(0));
                    optionalPeoples.put(fullName, patient);
                }
            } else {
                optionalPeoples.put(fullName, Optional.empty());
                shortMessage.append(fullName)
                        .append("Имя не распозналось из Excel\n");
            }
        }
        return optionalPeoples;
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
