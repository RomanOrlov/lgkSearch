package lgk.nsbc.model.dao;

import lgk.nsbc.generated.tables.records.NbcPatientsRecord;
import lgk.nsbc.model.Patients;
import lgk.nsbc.model.People;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static lgk.nsbc.generated.tables.BasPeople.BAS_PEOPLE;
import static lgk.nsbc.generated.tables.NbcPatients.NBC_PATIENTS;
import static lgk.nsbc.model.Patients.buildFromRecord;
import static org.jooq.impl.DSL.val;

@Service
public class PatientsDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    public Optional<Patients> getPatientByBasPeople(People people) {
        Result<NbcPatientsRecord> records = context.fetch(NBC_PATIENTS, NBC_PATIENTS.BAS_PEOPLE_N.eq(people.getN()));
        if (records.isEmpty()) return Optional.empty();
        if (records.size() != 1) throw new RuntimeException("One patient must have only one people");
        return Optional.of(buildFromRecord(records.get(0)));
    }

    public List<Patients> getPatientsWithSurnameLike(String surname) {
        Result<Record> records = context.select()
                .from(NBC_PATIENTS)
                .leftJoin(BAS_PEOPLE).on(NBC_PATIENTS.BAS_PEOPLE_N.eq(BAS_PEOPLE.N))
                .where(BAS_PEOPLE.SURNAME.likeIgnoreCase(val("%" + surname + "%")))
                .fetch();
        return getNbcPatientsAndBasPeople(records);
    }

    public List<Patients> getPatientsWithDifferetNames(String surname) {
        List<Patients> patientsWithSurnameLike = getPatientsWithSurnameLike(surname);
        Map<String, Patients> uniquePatients = patientsWithSurnameLike.stream().collect(toMap(
                patient -> {
                    People people = patient.getPeople();
                    String key = people.getSurname() + " " + people.getName() + " " + people.getPatronymic();
                    return key;
                }, patient -> patient, (o, o2) -> o
        ));
        return new ArrayList<>(uniquePatients.values());
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
