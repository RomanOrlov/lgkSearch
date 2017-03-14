package lgk.nsbc.template.dao;

import lgk.nsbc.generated.Sequences;
import lgk.nsbc.generated.tables.records.NbcFollowupRecord;
import lgk.nsbc.template.model.NbcFollowUp;
import lgk.nsbc.template.model.NbcStud;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        Result<NbcFollowupRecord> result = context.fetch(NBC_FOLLOWUP, NBC_FOLLOWUP.NBC_STUD_N.eq(nbcStud.getN()));
        return result.stream()
                .map(NbcFollowUp::buildFromRecord)
                .collect(toList());
    }
}
