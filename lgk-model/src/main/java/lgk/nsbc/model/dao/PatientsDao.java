package lgk.nsbc.model.dao;

import lgk.nsbc.model.Patients;
import lgk.nsbc.model.People;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static lgk.nsbc.generated.tables.BasPeople.BAS_PEOPLE;
import static lgk.nsbc.generated.tables.NbcPatients.NBC_PATIENTS;
import static org.jooq.impl.DSL.val;

@Service
public class PatientsDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    @Cacheable(cacheNames = "patientsByLikeFullName")
    public List<Patients> getPatientsWithFullNameLike(String fullName) {
        String[] splitedName = fullName.trim().replaceAll("[ ]{2,}", " ").split(" ");
        if (splitedName.length == 0)
            return Collections.emptyList();
        Condition likeCondition = BAS_PEOPLE.SURNAME.likeIgnoreCase(val("%" + splitedName[0] + "%"));
        if (splitedName.length > 1)
            likeCondition = likeCondition.and(BAS_PEOPLE.NAME.likeIgnoreCase(val("%" + splitedName[1] + "%")));
        if (splitedName.length > 2)
            likeCondition = likeCondition.and(BAS_PEOPLE.PATRONYMIC.likeIgnoreCase(val("%" + splitedName[2] + "%")));
        Result<Record> records = context.select()
                .from(NBC_PATIENTS)
                .leftJoin(BAS_PEOPLE).on(NBC_PATIENTS.BAS_PEOPLE_N.eq(BAS_PEOPLE.N))
                .where(likeCondition)
                .fetch();
        return getNbcPatientsAndBasPeople(records);
    }

    public List<Patients> getPatientsByFullName(String surname, String name, String patronymic) {
        Result<Record> records = context.select()
                .from(NBC_PATIENTS)
                .leftJoin(BAS_PEOPLE).on(NBC_PATIENTS.BAS_PEOPLE_N.eq(BAS_PEOPLE.N))
                .where(BAS_PEOPLE.SURNAME.equalIgnoreCase(surname)
                        .and(BAS_PEOPLE.NAME.equalIgnoreCase(name))
                        .and(BAS_PEOPLE.PATRONYMIC.equalIgnoreCase(patronymic)))
                .fetch();
        return getNbcPatientsAndBasPeople(records);
    }

    public List<Patients> findPatientsWithIdIn(List<Long> patientsId) {
        Result<Record> records = context.select()
                .from(NBC_PATIENTS)
                .leftJoin(BAS_PEOPLE).on(NBC_PATIENTS.BAS_PEOPLE_N.eq(BAS_PEOPLE.N))
                .where(NBC_PATIENTS.N.in(patientsId))
                .fetch();
        return getNbcPatientsAndBasPeople(records);
    }

    private List<Patients> getNbcPatientsAndBasPeople(Result<Record> records) {
        return records.stream()
                .map(record -> {
                    People people = People.buildFromRecord(record);
                    Patients patients = Patients.buildFromRecord(record);
                    patients.setPeople(people);
                    return patients;
                }).collect(toList());
    }
}
