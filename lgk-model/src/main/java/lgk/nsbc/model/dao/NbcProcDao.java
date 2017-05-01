package lgk.nsbc.model.dao;

import lgk.nsbc.model.NbcPatients;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.NbcProc.NBC_PROC;

@Service
public class NbcProcDao implements Serializable{
    @Autowired
    private DSLContext context;

    public int countProceduresForPatient(NbcPatients nbcPatients) {
        return context.fetchCount(NBC_PROC, NBC_PROC.NBC_PATIENTS_N.eq(nbcPatients.getN()));
    }
}
