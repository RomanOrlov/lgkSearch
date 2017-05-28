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
import static lgk.nsbc.generated.tables.Target.TARGET;
import static lgk.nsbc.generated.tables.TargetTargettype.TARGET_TARGETTYPE;

@Service
public class TargetDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    public int countTargetsForPatient(Patients patients) {
        return context.fetchCount(TARGET, TARGET.PATIENTS_N.eq(patients.getN()));
    }

    public List<Target> getPatientsTargets(Patients patients) {
        return context.select()
                .from(TARGET)
                .leftJoin(TARGET_TARGETTYPE).on(TARGET.TARGETTYPE.eq(TARGET_TARGETTYPE.N))
                .where(TARGET.PATIENTS_N.eq(patients.getN()))
                .fetch()
                .stream()
                .map(Target::buildFromRecord)
                .collect(toList());
    }

    public Target findTargetById(Long n) {
        Record record = context.select()
                .from(TARGET)
                .leftJoin(TARGET_TARGETTYPE).on(TARGET.TARGETTYPE.eq(TARGET_TARGETTYPE.N))
                .where(TARGET.N.in(n))
                .fetchOne();
        return Target.buildFromRecord(record);
    }
}
