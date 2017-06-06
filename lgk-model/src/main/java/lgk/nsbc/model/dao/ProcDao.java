package lgk.nsbc.model.dao;

import lgk.nsbc.model.Patients;
import lgk.nsbc.model.Proc;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static lgk.nsbc.generated.tables.Proc.PROC;

@Service
public class ProcDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    public List<Proc> findPatientProc(Patients patients) {
        return context.fetch(PROC, PROC.PATIENTS_N.eq(patients.getN()))
                .stream()
                .map(Proc::buildFromRecord)
                .collect(toList());
    }

    public List<Proc> findPatientsProcedures(List<Long> patientsId, Long procedureType) {
        return context.fetch(PROC, PROC.PATIENTS_N.in(patientsId)
                .and(PROC.PROC_TYPE.eq(procedureType)))
                .stream()
                .map(Proc::buildFromRecord)
                .collect(toList());
    }

    public List<Proc> findPatientsProcedures(List<Long> patientsId) {
        return context.fetch(PROC, PROC.PATIENTS_N.in(patientsId))
                .stream()
                .map(Proc::buildFromRecord)
                .collect(toList());
    }

    public int countProceduresForPatient(Patients patients) {
        return context.fetchCount(PROC, PROC.PATIENTS_N.eq(patients.getN()));
    }

    public void saveProc(Proc proc) {
        context.insertInto(PROC)
                .columns(PROC.N,
                        )
                .values()
                .returning(PROC.N);
    }

    public void updateProcDate(Proc surgeryProc) {

    }
}
