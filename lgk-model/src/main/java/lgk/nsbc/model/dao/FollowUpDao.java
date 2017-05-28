package lgk.nsbc.model.dao;

import lgk.nsbc.generated.Sequences;
import lgk.nsbc.generated.tables.records.FollowupRecord;
import lgk.nsbc.model.FollowUp;
import lgk.nsbc.model.Stud;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static lgk.nsbc.generated.tables.Followup.FOLLOWUP;
import static org.jooq.impl.DSL.val;

@Service
public class FollowUpDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    public void createFollowUp(FollowUp followUp) {
        Result<FollowupRecord> result = context.insertInto(FOLLOWUP)
                .columns(
                        FOLLOWUP.N,
                        FOLLOWUP.OP_CREATE,
                        FOLLOWUP.TARGET_N,
                        FOLLOWUP.STUD_N
                )
                .values(
                        Sequences.FOLLOWUP_N.nextval(),
                        Sequences.SYS_OPERATION_N.nextval(),
                        val(followUp.getTargetN()),
                        val(followUp.getStudN())
                )
                .returning(FOLLOWUP.N)
                .fetch();
        Long geberatedId = result.get(0).getN();
        followUp.setN(geberatedId);
    }

    public List<FollowUp> findByStudy(Stud stud) {
        return findByStudy(stud.getN());
    }

    public List<FollowUp> findByStudy(Long nbcStudId) {
        Result<FollowupRecord> result = context.fetch(FOLLOWUP, FOLLOWUP.STUD_N.eq(nbcStudId));
        return result.stream()
                .map(FollowUp::buildFromRecord)
                .collect(toList());
    }

    public FollowUp findById(Long id) {
        FollowupRecord nbcFollowupRecord = context.fetchOne(FOLLOWUP, FOLLOWUP.N.eq(id));
        return FollowUp.buildFromRecord(nbcFollowupRecord);
    }

    public void deleteFollowUp(Collection<FollowUp> followUps) {
        List<Long> nbcFollowUpIds = followUps.stream()
                .map(FollowUp::getN)
                .collect(toList());
        int removedRecords = context.deleteFrom(FOLLOWUP)
                .where(FOLLOWUP.N.in(nbcFollowUpIds))
                .execute();
    }

    public void deleteFollowUp(FollowUp followUps) {
        int removedRecords = context.deleteFrom(FOLLOWUP)
                .where(FOLLOWUP.N.eq(followUps.getN()))
                .execute();
    }
}
