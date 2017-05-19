package lgk.nsbc.model.dao;

import lgk.nsbc.model.Patients;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.NbcProc.NBC_PROC;

@Service
public class ProcDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    public int countProceduresForPatient(Patients patients) {
        return context.fetchCount(NBC_PROC, NBC_PROC.NBC_PATIENTS_N.eq(patients.getN()));
    }
}
