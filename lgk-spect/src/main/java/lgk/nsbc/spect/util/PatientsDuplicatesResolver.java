package lgk.nsbc.spect.util;

import lgk.nsbc.model.People;
import lgk.nsbc.model.dao.PatientsDao;
import lgk.nsbc.model.dao.ProcDao;
import lgk.nsbc.model.dao.StudDao;
import lgk.nsbc.model.dao.TargetDao;
import lgk.nsbc.model.People;
import lgk.nsbc.model.Patients;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
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
@RequiredArgsConstructor
public class PatientsDuplicatesResolver implements Serializable{
    private final PatientsDao patientsDao;
    private final StudDao studDao;
    private final ProcDao procDao;
    private final TargetDao targetDao;

    public Optional<Patients> getPatient(Patients patients) {
        People people = patients.getPeople();
        return getPatient(people.getSurname(), people.getName(), people.getPatronymic());
    }

    public Optional<Patients> getPatient(People people) {
        return getPatient(people.getSurname(), people.getName(), people.getPatronymic());
    }

    public Optional<Patients> getPatient(String surname, String name, String patronymic) {
        List<Patients> patientsList = patientsDao.getPatientsByFullName(surname, name, patronymic);
        if (patientsList.isEmpty()) return Optional.empty();
        if (patientsList.size() == 1) return Optional.of(patientsList.get(0));

        Map<Patients, Boolean> map = patientsList.stream()
                .collect(toMap(identity(), studDao::isPatientHasSpectStudy));
        if (map.containsValue(true)) {
            long countOfTrue = map.values()
                    .stream()
                    .filter(Boolean::booleanValue)
                    .count();
            if (countOfTrue == 1) {
                Patients patients = map.keySet()
                        .stream()
                        .filter(map::get)
                        .findFirst().orElseThrow(RuntimeException::new);
                return Optional.of(patients);
            }
        }

        List<PatientRecordsCount> counts = patientsList.stream()
                .map(nbcPatients -> new PatientRecordsCount(nbcPatients,
                        procDao.countProceduresForPatient(nbcPatients),
                        targetDao.countTargetsForPatient(nbcPatients)))
                .sorted()
                .collect(Collectors.toList());
        Patients patients = counts.get(0).getPatients();
        return Optional.of(patients);
    }

    @Getter
    @AllArgsConstructor
    private static class PatientRecordsCount implements Comparable<PatientRecordsCount> {
        private Patients patients;
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
            return patients.getN().compareTo(this.getPatients().getN());
        }
    }
}
