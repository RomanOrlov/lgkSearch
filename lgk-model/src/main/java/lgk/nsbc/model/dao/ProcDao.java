package lgk.nsbc.model.dao;

import lgk.nsbc.generated.Sequences;
import lgk.nsbc.generated.tables.records.ProcRecord;
import lgk.nsbc.model.DateUtils;
import lgk.nsbc.model.Patients;
import lgk.nsbc.model.Proc;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.util.calendar.BaseCalendar;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static lgk.nsbc.generated.tables.Proc.PROC;
import static org.jooq.impl.DSL.val;

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
        Timestamp timestamp = DateUtils.fromDateToTimestamp(proc.getProcBeginTime());
        ProcRecord procRecord = context.insertInto(PROC)
                .columns(PROC.N,
                        PROC.OP_CREATE,
                        PROC.PATIENTS_N,
                        PROC.PROC_TYPE,
                        PROC.PROCBEGINTIME,
                        PROC.TIME_APPROX)
                .values(Sequences.PROC_N.nextval(),
                        Sequences.SYS_OPERATION_N.nextval(),
                        val(proc.getPatientN()),
                        val(proc.getProcType().getN()),
                        val(timestamp),
                        val(proc.getProcTimeApprox().getN()))
                .returning(PROC.N)
                .fetchOne();
        proc.setN(procRecord.getN());
    }

    public void updateProcDate(Proc surgeryProc) {
        Timestamp procBeginTime = DateUtils.fromDateToTimestamp(surgeryProc.getProcBeginTime());
        context.update(PROC)
                .set(PROC.PROCBEGINTIME, procBeginTime)
                .where(PROC.N.eq(surgeryProc.getN()))
                .execute();
    }
}
