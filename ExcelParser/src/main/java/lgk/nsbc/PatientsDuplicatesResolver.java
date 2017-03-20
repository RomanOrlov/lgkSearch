package lgk.nsbc;

import lgk.nsbc.template.dao.NbcPatientsDao;
import lgk.nsbc.template.model.BasPeople;
import lgk.nsbc.template.model.NbcPatients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<NbcPatients> getPatient(NbcPatients nbcPatients) {
        BasPeople basPeople = nbcPatients.getBasPeople();
        return getPatient(basPeople.getSurname(), basPeople.getName(), basPeople.getPatronymic());


    }

    public Optional<NbcPatients> getPatient(String surname, String name, String patronymic) {
        List<NbcPatients> nbcPatientsList = nbcPatientsDao.getPatientsByFullName(surname, name, patronymic);
        if (nbcPatientsList.isEmpty()) return Optional.empty();
        if (nbcPatientsList.size() == 1) return Optional.of(nbcPatientsList.get(0));

    }
}
