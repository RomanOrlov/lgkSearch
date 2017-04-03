package lgk.nsbc.dao;

import lgk.nsbc.generated.Sequences;
import lgk.nsbc.generated.tables.records.NbcFollowupRecord;
import lgk.nsbc.model.NbcFollowUp;
import lgk.nsbc.model.NbcStud;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static lgk.nsbc.generated.tables.NbcFollowup.NBC_FOLLOWUP;
import static org.jooq.impl.DSL.val;

@Service
public class NbcFollowUpDao {
    @Autowired
    private DSLContext context;

    public void createFollowUp(NbcFollowUp nbcFollowUp) {
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
                        val(nbcFollowUp.getNbc_target_n()),
                        val(nbcFollowUp.getNbc_stud_n())
                )
                .returning(NBC_FOLLOWUP.N)
                .fetch();
        Long geberatedId = result.get(0).getN();
        nbcFollowUp.setN(geberatedId);
    }

    public List<NbcFollowUp> findByStudy(NbcStud nbcStud) {
        return findByStudy(nbcStud.getN());
    }

    public List<NbcFollowUp> findByStudy(Long nbcStudId) {
        Result<NbcFollowupRecord> result = context.fetch(NBC_FOLLOWUP, NBC_FOLLOWUP.NBC_STUD_N.eq(nbcStudId));
        return result.stream()
                .map(NbcFollowUp::buildFromRecord)
                .collect(toList());
    }

    public NbcFollowUp findById(Long id) {
        NbcFollowupRecord nbcFollowupRecord = context.fetchOne(NBC_FOLLOWUP, NBC_FOLLOWUP.N.eq(id));
        return NbcFollowUp.buildFromRecord(nbcFollowupRecord);
    }

    public void deleteFollowUp(Collection<NbcFollowUp> nbcFollowUps) {
        List<Long> nbcFollowUpIds = nbcFollowUps.stream()
                .map(NbcFollowUp::getN)
                .collect(toList());
        int removedRecords = context.deleteFrom(NBC_FOLLOWUP)
                .where(NBC_FOLLOWUP.N.in(nbcFollowUpIds))
                .execute();
    }
}
