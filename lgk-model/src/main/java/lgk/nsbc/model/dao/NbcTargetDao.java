package lgk.nsbc.model.dao;

import lgk.nsbc.model.NbcPatients;
import lgk.nsbc.model.NbcTarget;
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
public class NbcTargetDao implements Serializable {
    @Autowired
    private DSLContext context;

    public int countTargetsForPatient(NbcPatients nbcPatients) {
        return context.fetchCount(NBC_TARGET, NBC_TARGET.NBC_PATIENTS_N.eq(nbcPatients.getN()));
    }

    public List<NbcTarget> getPatientsTargets(NbcPatients nbcPatients) {
        return context.select()
                .from(NBC_TARGET)
                .leftJoin(NBC_TARGET_TARGETTYPE).on(NBC_TARGET.TARGETTYPE.eq(NBC_TARGET_TARGETTYPE.N))
                .where(NBC_TARGET.NBC_PATIENTS_N.eq(nbcPatients.getN()))
                .fetch()
                .stream()
                .map(NbcTarget::buildFromRecord)
                .collect(toList());
    }

    public NbcTarget findTargetById(Long n) {
        Record record = context.select()
                .from(NBC_TARGET)
                .leftJoin(NBC_TARGET_TARGETTYPE).on(NBC_TARGET.TARGETTYPE.eq(NBC_TARGET_TARGETTYPE.N))
                .where(NBC_TARGET.N.in(n))
                .fetchOne();
        return NbcTarget.buildFromRecord(record);
    }
}
