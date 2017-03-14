package lgk.nsbc.template.dao;

import lgk.nsbc.generated.tables.records.NbcPatientsRecord;
import lgk.nsbc.template.model.BasPeople;
import lgk.nsbc.template.model.NbcPatients;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static lgk.nsbc.generated.tables.BasPeople.BAS_PEOPLE;
import static lgk.nsbc.generated.tables.NbcPatients.NBC_PATIENTS;
import static lgk.nsbc.template.model.NbcPatients.buildFromRecord;

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
                .where(BAS_PEOPLE.SURNAME.likeIgnoreCase(surname))
                .fetch();
        List<NbcPatients> nbcPatientsList = records.stream()
                .map(record -> {
                    BasPeople basPeople = BasPeople.buildFromRecord(record);
                    NbcPatients patients = NbcPatients.buildFromRecord(record);
                    patients.setBasPeople(basPeople);
                    return patients;
                }).collect(toList());
        return nbcPatientsList;
    }
}
