package lgk.nsbc.model.dao;

import lgk.nsbc.model.Patients;
import lgk.nsbc.model.Target;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static lgk.nsbc.generated.tables.Target.TARGET;

@Service
public class TargetDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    public int countTargetsForPatient(Patients patients) {
        return context.fetchCount(TARGET, TARGET.PATIENTS_N.eq(patients.getN()));
    }

    public List<Target> getPatientsTargets(Patients patients) {
        return context.fetch(TARGET, TARGET.PATIENTS_N.eq(patients.getN()))
                .stream()
                .map(Target::buildFromRecord)
                .collect(toList());
    }

    public List<Target> getTargetsById(List<Long> targetsId) {
        return context.fetch(TARGET, TARGET.N.in(targetsId))
                .stream()
                .map(Target::buildFromRecord)
                .collect(toList());
    }
}
