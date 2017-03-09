package lgk.nsbc.template.dao;

import lgk.nsbc.template.model.NbcPatients;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import static lgk.nsbc.generated.tables.NbcTarget.NBC_TARGET;

@Service
public class NbcTargetDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private DSLContext context;

    public int countTargetsForPatient(NbcPatients nbcPatients) {
        return context.fetchCount(NBC_TARGET, NBC_TARGET.NBC_PATIENTS_N.eq(nbcPatients.getN()));
    }
}
