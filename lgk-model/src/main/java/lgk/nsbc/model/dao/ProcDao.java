package lgk.nsbc.model.dao;

import lgk.nsbc.model.Patients;
import lgk.nsbc.model.Proc;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static lgk.nsbc.generated.tables.NbcProc.NBC_PROC;

@Service
public class ProcDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    public List<Proc> findPatientProc(Patients patients) {
        return context.fetch(NBC_PROC, NBC_PROC.NBC_PATIENTS_N.eq(patients.getN()))
                .stream()
                .map(Proc::buildFromRecord)
                .collect(toList());
    }

    public List<Proc> findPatientsProcedures(List<Long> patientsId, Long procedureType) {
        return context.fetch(NBC_PROC, NBC_PROC.NBC_PATIENTS_N.in(patientsId)
                .and(NBC_PROC.PROC_TYPE.eq(procedureType)))
                .stream()
                .map(Proc::buildFromRecord)
                .collect(toList());
    }

    public int countProceduresForPatient(Patients patients) {
        return context.fetchCount(NBC_PROC, NBC_PROC.NBC_PATIENTS_N.eq(patients.getN()));
    }
}
