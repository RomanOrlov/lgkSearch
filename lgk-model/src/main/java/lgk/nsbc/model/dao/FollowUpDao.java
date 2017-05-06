package lgk.nsbc.model.dao;

import lgk.nsbc.generated.Sequences;
import lgk.nsbc.generated.tables.records.NbcFollowupRecord;
import lgk.nsbc.model.FollowUp;
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
import static lgk.nsbc.generated.tables.NbcFollowup.NBC_FOLLOWUP;
import static org.jooq.impl.DSL.val;

@Service
public class FollowUpDao implements Serializable{
    @Autowired
    private DSLContext context;

    public void createFollowUp(FollowUp followUp) {
        Result<NbcFollowupRecord> result = context.insertInto(NBC_FOLLOWUP)
                .columns(
                        NBC_FOLLOWUP.N,
                        NBC_FOLLOWUP.OP_CREATE,
                        NBC_FOLLOWUP.NBC_TARGET_N,
                        NBC_FOLLOWUP.NBC_STUD_N
                )
                .values(
                        Sequences.NBC_FOLLOWUP_N.nextval(),
                        Sequences.SYS_OPERATION_N.nextval(),
                        val(followUp.getNbc_target_n()),
                        val(followUp.getNbc_stud_n())
                )
                .returning(NBC_FOLLOWUP.N)
                .fetch();
        Long geberatedId = result.get(0).getN();
        followUp.setN(geberatedId);
    }

    public List<FollowUp> findByStudy(Stud stud) {
        return findByStudy(stud.getN());
    }

    public List<FollowUp> findByStudy(Long nbcStudId) {
        Result<NbcFollowupRecord> result = context.fetch(NBC_FOLLOWUP, NBC_FOLLOWUP.NBC_STUD_N.eq(nbcStudId));
        return result.stream()
                .map(FollowUp::buildFromRecord)
                .collect(toList());
    }

    public FollowUp findById(Long id) {
        NbcFollowupRecord nbcFollowupRecord = context.fetchOne(NBC_FOLLOWUP, NBC_FOLLOWUP.N.eq(id));
        return FollowUp.buildFromRecord(nbcFollowupRecord);
    }

    public void deleteFollowUp(Collection<FollowUp> followUps) {
        List<Long> nbcFollowUpIds = followUps.stream()
                .map(FollowUp::getN)
                .collect(toList());
        int removedRecords = context.deleteFrom(NBC_FOLLOWUP)
                .where(NBC_FOLLOWUP.N.in(nbcFollowUpIds))
                .execute();
    }

    public void deleteFollowUp(FollowUp followUps) {
        int removedRecords = context.deleteFrom(NBC_FOLLOWUP)
                .where(NBC_FOLLOWUP.N.eq(followUps.getN()))
                .execute();
    }
}
