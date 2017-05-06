package lgk.nsbc.model.dao;

import lgk.nsbc.model.Patients;
import lgk.nsbc.model.Target;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static lgk.nsbc.generated.tables.NbcTarget.NBC_TARGET;
import static lgk.nsbc.generated.tables.NbcTargetTargettype.NBC_TARGET_TARGETTYPE;

@Service
public class TargetDao implements Serializable {
    @Autowired
    private DSLContext context;

    public int countTargetsForPatient(Patients patients) {
        return context.fetchCount(NBC_TARGET, NBC_TARGET.NBC_PATIENTS_N.eq(patients.getN()));
    }

    public List<Target> getPatientsTargets(Patients patients) {
        return context.select()
                .from(NBC_TARGET)
                .leftJoin(NBC_TARGET_TARGETTYPE).on(NBC_TARGET.TARGETTYPE.eq(NBC_TARGET_TARGETTYPE.N))
                .where(NBC_TARGET.NBC_PATIENTS_N.eq(patients.getN()))
                .fetch()
                .stream()
                .map(Target::buildFromRecord)
                .collect(toList());
    }

    public Target findTargetById(Long n) {
        Record record = context.select()
                .from(NBC_TARGET)
                .leftJoin(NBC_TARGET_TARGETTYPE).on(NBC_TARGET.TARGETTYPE.eq(NBC_TARGET_TARGETTYPE.N))
                .where(NBC_TARGET.N.in(n))
                .fetchOne();
        return Target.buildFromRecord(record);
    }
}
