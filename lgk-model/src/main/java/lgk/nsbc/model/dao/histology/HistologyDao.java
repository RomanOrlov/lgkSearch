package lgk.nsbc.model.dao.histology;

import lgk.nsbc.generated.tables.records.NbcHistology_1Record;
import lgk.nsbc.model.Patients;
import lgk.nsbc.model.Stud;
import lgk.nsbc.model.histology.Histology;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import static lgk.nsbc.generated.tables.NbcHistology_1.NBC_HISTOLOGY_1;

@Service
public class HistologyDao implements Serializable {
    @Autowired
    private DSLContext context;

    public List<Histology> findByPatient(Patients patients) {
        Result<NbcHistology_1Record> result = context.fetch(NBC_HISTOLOGY_1, NBC_HISTOLOGY_1.NBC_PATIENTS_N.eq(patients.getN()));
        return result.stream()
                .map(Histology::buildFromRecord)
                .collect(Collectors.toList());
    }

    public List<Histology> findByStudy(Stud stud) {
        Result<NbcHistology_1Record> result = context.fetch(NBC_HISTOLOGY_1, NBC_HISTOLOGY_1.NBC_STUD_N.eq(stud.getN()));
        return result.stream()
                .map(Histology::buildFromRecord)
                .collect(Collectors.toList());
    }
}
