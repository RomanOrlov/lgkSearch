package lgk.nsbc;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.spring.annotation.VaadinSessionScope;
import lgk.nsbc.backend.entity.BasPeople;
import lgk.nsbc.backend.entity.NbcPatients;
import lgk.nsbc.dao.BasPeopleRepository;
import lgk.nsbc.dao.NbcPatientsRepository;
import lgk.nsbc.dao.NbcProcRepository;
import lgk.nsbc.dao.NbcTargetRepository;
import lgk.nsbc.model.StudyRecords;
import lgk.nsbc.view.ParsingView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
@VaadinSessionScope
public class DataMigrationService {
    @Autowired
    private ParserService parserService;
    @Autowired
    private BasPeopleRepository basPeopleRepository;
    @Autowired
    private NbcPatientsRepository nbcPatientsRepository;
    @Autowired
    private NbcProcRepository nbcProcRepository;
    @Autowired
    private NbcTargetRepository nbcTargetRepository;
    private ParsingView parsingView;

    private Map<String, TreeSet<StudyRecords>> records;
    private Map<String, NbcPatients> patients = new TreeMap<>();
    private Map<String, PatientSearchInfo> searchedPatients;


    public void findPatients(File tempFile) {
        try {
            records = mapByFullName(tempFile);
            searchedPatients = parseNames(records.keySet());
            String parsingInfo = getParsingInfo(searchedPatients);
            Map<String, IndexedContainer> duplicatePatients = searchedPatients.entrySet().stream()
                    .filter(entry -> entry.getValue().getSearchResult() == SearchResult.IMPOSSIBLE_TO_IDENTIFY)
                    .collect(toMap(Map.Entry::getKey, e -> getIndexedContainerForDuplicates(e.getKey(), e.getValue())));

            searchedPatients.entrySet().stream()
                    .filter(entry -> entry.getValue().getSearchResult() == SearchResult.PARSING_SUCCESS)
                    .forEach(entry -> patients.put(entry.getKey(), entry.getValue().getNbcPatients().get(0)));

            parsingView = new ParsingView(this, parsingInfo, duplicatePatients);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private IndexedContainer getIndexedContainerForDuplicates(String name, PatientSearchInfo patientSearchInfo) {
        List<NbcPatients> nbcPatients = patientSearchInfo.getNbcPatients();
        IndexedContainer indexedContainer = new IndexedContainer();
        indexedContainer.addContainerProperty("Имя", String.class, null);
        indexedContainer.addContainerProperty("ID", Integer.class, null);
        indexedContainer.addContainerProperty("№ Истории болезни", Integer.class, null);
        for (NbcPatients patient : nbcPatients) {
            Item item = indexedContainer.addItem(patient.getN());
            item.getItemProperty("Имя").setValue(name);
            item.getItemProperty("ID").setValue(patient.getN());
            item.getItemProperty("№ Истории болезни").setValue(patient.getCaseHistoryNum());
        }
        return indexedContainer;
    }

    public void addResolvedConflicts(Map<String, Object> selectedPatients) {
        for (Map.Entry<String, Object> entry : selectedPatients.entrySet()) {
            String name = entry.getKey();
            PatientSearchInfo patientSearchInfo = searchedPatients.get(name);
            Integer id = (Integer) entry.getValue();
            NbcPatients patient = patientSearchInfo.getNbcPatients().stream()
                    .filter(nbcPatients -> Objects.equals(nbcPatients.getN(), id))
                    .findFirst().orElseThrow(RuntimeException::new);
            patients.put(name, patient);
        }
    }

    /**
     * Должен проверить, что такой поцедуры нет.
     * Сохраняет процедуры у пациентов.
     */
    public void saveDataToDB() {

    }

    private String getParsingInfo(Map<String, PatientSearchInfo> peoples) {
        StringBuilder message = new StringBuilder();
        message.append("Данные следующих пациентов были успено добавлены в базу:\n");
        peoples.entrySet().stream()
                .filter(entry -> entry.getValue().getSearchResult() == SearchResult.PARSING_SUCCESS)
                .forEach(entry -> message.append(entry.getKey()).append("\n"));
        message.append("\n");
        message.append("Следующие пациенты не были найдены в базе (либо они отсуттствуют либо невозможно разобрать имя):\n");
        peoples.entrySet().stream()
                .filter(entry -> entry.getValue().getSearchResult() == SearchResult.NO_SUCH_PATIENT ||
                        entry.getValue().getSearchResult() == SearchResult.IMPOSSIBLE_PARSE_NAME)
                .forEach(entry -> message.append(entry.getKey()).append("\n"));
        message.append("\n");
        message.append("Возникли конфликты при попытке распознать пациентов:\n");
        peoples.entrySet().stream()
                .filter(entry -> entry.getValue().getSearchResult() == SearchResult.IMPOSSIBLE_TO_IDENTIFY)
                .forEach(entry -> message.append(entry.getKey()).append("\n"));
        return message.toString();
    }

    private Map<String, PatientSearchInfo> parseNames(Set<String> names) {
        Map<String, PatientSearchInfo> peoples = new TreeMap<>();
        List<String> surnames = names.stream()
                .map(name -> name.trim().split(" ")[0])
                .collect(toList());
        // Get all data from db in one time
        List<BasPeople> basPeoples = basPeopleRepository.findBySurnameIn(surnames);
        Map<String[], BasPeople> basPeopleMap = basPeoples.stream()
                .collect(toMap(people -> new String[]{people.getSurname(), people.getName(), people.getPatronymic()}, identity()));
        for (String fullName : names) {
            String[] parsedName = fullName.trim().split(" ");
            if (parsedName.length == 3) {
                List<BasPeople> man = basPeopleMap.entrySet().stream()
                        .filter(entry -> Arrays.deepEquals(entry.getKey(), parsedName))
                        .map(Map.Entry::getValue)
                        .collect(toList());
                if (man.isEmpty()) {
                    peoples.put(fullName, new PatientSearchInfo(emptyList(), SearchResult.NO_SUCH_PATIENT));
                } else {
                    List<NbcPatients> nbcPatients = getPatients(man);
                    SearchResult searchResult = nbcPatients.size() == 1 ? SearchResult.PARSING_SUCCESS : SearchResult.IMPOSSIBLE_TO_IDENTIFY;
                    peoples.put(fullName, new PatientSearchInfo(nbcPatients, searchResult));
                }
            } else {
                peoples.put(fullName, new PatientSearchInfo(emptyList(), SearchResult.IMPOSSIBLE_PARSE_NAME));
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

    private List<NbcPatients> getPatients(List<BasPeople> basPeoples) {
        if (basPeoples.size() == 1) {
            NbcPatients patient = nbcPatientsRepository.findByBasPeople(basPeoples.get(0));
            return Collections.singletonList(patient); // Normally
        }
        // Должен принадлежать радиологии и быть единственным на кого навешены все процедуры и мишени
        List<NbcPatients> patients = basPeoples.stream()
                .map(nbcPatientsRepository::findByBasPeople)
                .collect(toList());

        List<NbcPatients> filtered = patients.stream()
                .filter(nbcPatients -> nbcPatients.getNbcOrganizations().getN() == 11)
                .filter(patient -> nbcProcRepository.countByNbcPatients(patient) == 0)
                .filter(patient -> nbcTargetRepository.countByNbcPatients(patient) == 0)
                .collect(toList());

        return filtered.size() == 1 ? filtered : patients;
    }

    @Getter
    private enum SearchResult {
        PARSING_SUCCESS("Найден уникальный пациент с данным именем"),
        IMPOSSIBLE_PARSE_NAME("Невозможно автоматически определить имя из файла"),
        NO_SUCH_PATIENT("Пациент с таким именем не существует в базе данных"),
        IMPOSSIBLE_TO_IDENTIFY("Существует несколько пациентов с таким именем. Невозможно определить нужного.");
        private final String message;

        SearchResult(String message) {
            this.message = message;
        }
    }

    @Getter
    @AllArgsConstructor
    private static class PatientSearchInfo {
        private final List<NbcPatients> nbcPatients;
        private final SearchResult searchResult;
    }
}
