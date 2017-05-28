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
import static lgk.nsbc.generated.tables.People.PEOPLE;
import static lgk.nsbc.generated.tables.Patients.PATIENTS;
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
        Condition likeCondition = PEOPLE.SURNAME.likeIgnoreCase(val("%" + splitedName[0] + "%"));
        if (splitedName.length > 1)
            likeCondition = likeCondition.and(PEOPLE.NAME.likeIgnoreCase(val("%" + splitedName[1] + "%")));
        if (splitedName.length > 2)
            likeCondition = likeCondition.and(PEOPLE.PATRONYMIC.likeIgnoreCase(val("%" + splitedName[2] + "%")));
        Result<Record> records = context.select()
                .from(PATIENTS)
                .leftJoin(PEOPLE).on(PATIENTS.PEOPLE_N.eq(PEOPLE.N))
                .where(likeCondition)
                .fetch();
        return getPatientsAndPeople(records);
    }

    public List<Patients> getPatientsByFullName(String surname, String name, String patronymic) {
        Result<Record> records = context.select()
                .from(PATIENTS)
                .leftJoin(PEOPLE).on(PATIENTS.PEOPLE_N.eq(PEOPLE.N))
                .where(PEOPLE.SURNAME.equalIgnoreCase(surname)
                        .and(PEOPLE.NAME.equalIgnoreCase(name))
                        .and(PEOPLE.PATRONYMIC.equalIgnoreCase(patronymic)))
                .fetch();
        return getPatientsAndPeople(records);
    }

    public List<Patients> findPatientsWithIdIn(List<Long> patientsId) {
        Result<Record> records = context.select()
                .from(PATIENTS)
                .leftJoin(PEOPLE).on(PATIENTS.PEOPLE_N.eq(PEOPLE.N))
                .where(PATIENTS.N.in(patientsId))
                .fetch();
        return getPatientsAndPeople(records);
    }

    private List<Patients> getPatientsAndPeople(Result<Record> records) {
        return records.stream()
                .map(record -> {
                    People people = People.buildFromRecord(record);
                    Patients patients = Patients.buildFromRecord(record);
                    patients.setPeople(people);
                    return patients;
                }).collect(toList());
    }
}
