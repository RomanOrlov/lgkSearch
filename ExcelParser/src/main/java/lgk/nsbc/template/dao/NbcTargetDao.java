package lgk.nsbc.template.dao;

import lgk.nsbc.generated.tables.records.NbcTargetRecord;
import lgk.nsbc.template.model.NbcPatients;
import lgk.nsbc.template.model.NbcTarget;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static lgk.nsbc.generated.tables.NbcTarget.NBC_TARGET;

@Service
public class NbcTargetDao {
    @Autowired
    private DSLContext context;

    public int countTargetsForPatient(NbcPatients nbcPatients) {
        return context.fetchCount(NBC_TARGET, NBC_TARGET.NBC_PATIENTS_N.eq(nbcPatients.getN()));
    }

    public List<NbcTarget> getPatientsTargets(NbcPatients nbcPatients) {
        Result<NbcTargetRecord> records = context.fetch(NBC_TARGET, NBC_TARGET.NBC_PATIENTS_N.eq(nbcPatients.getN()));
        return records.stream()
                .map(NbcTarget::buildFromRecord)
                .collect(toList());
    }

    public NbcTarget findTargetById(Long n) {
        NbcTargetRecord nbcTargetRecord = context.fetchOne(NBC_TARGET, NBC_TARGET.N.eq(n));
        return NbcTarget.buildFromRecord(nbcTargetRecord);
    }
}
