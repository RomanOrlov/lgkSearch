package lgk.nsbc;

import lgk.nsbc.template.dao.NbcPatientsDao;
import lgk.nsbc.template.dao.NbcProcDao;
import lgk.nsbc.template.dao.NbcStudDao;
import lgk.nsbc.template.dao.NbcTargetDao;
import lgk.nsbc.template.model.BasPeople;
import lgk.nsbc.template.model.NbcPatients;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/**
 * Класс, который пытается определить из списка пациентов с одинаковы именами нужного.
 * 1. Проверяем не один ли пациент с таким полным именем.
 * 2. Проверяем есть ли запись о исследовании в NBC_STUD с Типом процедуры ОФЕКТ.
 * 3. Проверяем количество процедур у пациента.
 * Выбираем того, у которого больше всего процедур и мишеней (сначала мишеней, потом процедур)
 * В случае, если всё по нулям (несколько пустых пациентов) выбираем того, у которого N больше.
 */
@Service
public class PatientsDuplicatesResolver {
    @Autowired
    private NbcPatientsDao nbcPatientsDao;
    @Autowired
    private NbcStudDao nbcStudDao;
    @Autowired
    private NbcProcDao nbcProcDao;
    @Autowired
    private NbcTargetDao nbcTargetDao;

    public Optional<NbcPatients> getPatient(NbcPatients nbcPatients) {
        BasPeople basPeople = nbcPatients.getBasPeople();
        return getPatient(basPeople.getSurname(), basPeople.getName(), basPeople.getPatronymic());
    }

    public Optional<NbcPatients> getPatient(String surname, String name, String patronymic) {
        List<NbcPatients> nbcPatientsList = nbcPatientsDao.getPatientsByFullName(surname, name, patronymic);
        if (nbcPatientsList.isEmpty()) return Optional.empty();
        if (nbcPatientsList.size() == 1) return Optional.of(nbcPatientsList.get(0));

        Map<NbcPatients, Boolean> map = nbcPatientsList.stream()
                .collect(toMap(identity(), patients -> nbcStudDao.isPatientHasSpectStudy(patients)));
        if (map.containsValue(true)) {
            long countOfTrue = map.values()
                    .stream()
                    .filter(Boolean::booleanValue)
                    .count();
            if (countOfTrue == 1) {
                NbcPatients patients = map.keySet()
                        .stream()
                        .filter(map::get)
                        .findFirst().orElseThrow(RuntimeException::new);
                return Optional.of(patients);
            }
        }

        List<PatientRecordsCount> counts = nbcPatientsList.stream()
                .map(nbcPatients -> new PatientRecordsCount(nbcPatients,
                        nbcProcDao.countProceduresForPatient(nbcPatients),
                        nbcTargetDao.countTargetsForPatient(nbcPatients)))
                .sorted()
                .collect(Collectors.toList());
        NbcPatients nbcPatients = counts.get(0).getNbcPatients();
        return Optional.of(nbcPatients);
    }

    @Getter
    @AllArgsConstructor
    private static class PatientRecordsCount implements Comparable<PatientRecordsCount> {
        private NbcPatients nbcPatients;
        private Integer targetsCount;
        private Integer proceduresCount;

        @Override
        public int compareTo(PatientRecordsCount o) {
            int i = targetsCount.compareTo(o.getTargetsCount());
            if (i != 0)
                return i;
            i = proceduresCount.compareTo(o.getProceduresCount());
            if (i != 0)
                return i;
            return nbcPatients.getN().compareTo(o.getNbcPatients().getN());
        }
    }
}
