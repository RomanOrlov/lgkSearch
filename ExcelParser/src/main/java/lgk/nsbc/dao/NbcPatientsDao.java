package lgk.nsbc.dao;

import lgk.nsbc.generated.tables.records.NbcPatientsRecord;
import lgk.nsbc.model.BasPeople;
import lgk.nsbc.model.NbcPatients;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static lgk.nsbc.generated.tables.BasPeople.BAS_PEOPLE;
import static lgk.nsbc.generated.tables.NbcPatients.NBC_PATIENTS;
import static lgk.nsbc.model.NbcPatients.buildFromRecord;
import static org.jooq.impl.DSL.val;

@Service
public class NbcPatientsDao {
    @Autowired
    private DSLContext context;

    public Optional<NbcPatients> getPatientByBasPeople(BasPeople basPeople) {
        Result<NbcPatientsRecord> records = context.fetch(NBC_PATIENTS, NBC_PATIENTS.BAS_PEOPLE_N.eq(basPeople.getN()));
        if (records.isEmpty()) return Optional.empty();
        if (records.size() != 1) throw new RuntimeException("One patient must have only one people");
        return Optional.of(buildFromRecord(records.get(0)));
    }

    public List<NbcPatients> getPatientsWithSurnameLike(String surname) {
        Result<Record> records = context.select()
                .from(NBC_PATIENTS)
                .leftJoin(BAS_PEOPLE).on(NBC_PATIENTS.BAS_PEOPLE_N.eq(BAS_PEOPLE.N))
                .where(BAS_PEOPLE.SURNAME.likeIgnoreCase(val("%" + surname + "%")))
                .fetch();
        return getNbcPatientsAndBasPeople(records);
    }

    public List<NbcPatients> getPatientsWithDifferetNames(String surname) {
        List<NbcPatients> patientsWithSurnameLike = getPatientsWithSurnameLike(surname);
        Map<String, NbcPatients> uniquePatients = patientsWithSurnameLike.stream().collect(toMap(
                patient -> {
                    BasPeople basPeople = patient.getBasPeople();
                    String key = basPeople.getSurname() + " " + basPeople.getName() + " " + basPeople.getPatronymic();
                    return key;
                },
                patient -> patient,
                (o, o2) -> o
        ));
        return new ArrayList<>(uniquePatients.values());
    }

    public List<NbcPatients> getPatientsByFullName(String surname, String name, String patronymic) {
        Result<Record> records = context.select()
                .from(NBC_PATIENTS)
                .leftJoin(BAS_PEOPLE).on(NBC_PATIENTS.BAS_PEOPLE_N.eq(BAS_PEOPLE.N))
                .where(BAS_PEOPLE.SURNAME.equalIgnoreCase(surname)
                        .and(BAS_PEOPLE.NAME.equalIgnoreCase(name))
                        .and(BAS_PEOPLE.PATRONYMIC.equalIgnoreCase(patronymic)))
                .fetch();
        return getNbcPatientsAndBasPeople(records);
    }

    private List<NbcPatients> getNbcPatientsAndBasPeople(Result<Record> records) {
        return records.stream()
                .map(record -> {
                    BasPeople basPeople = BasPeople.buildFromRecord(record);
                    NbcPatients patients = NbcPatients.buildFromRecord(record);
                    patients.setBasPeople(basPeople);
                    return patients;
                }).collect(toList());
    }
}
