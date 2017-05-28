package lgk.nsbc.model.dao;

import lgk.nsbc.generated.Sequences;
import lgk.nsbc.generated.tables.records.StudInjRecord;
import lgk.nsbc.model.Stud;
import lgk.nsbc.model.StudInj;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Optional;

import static lgk.nsbc.generated.tables.StudInj.STUD_INJ;
import static org.jooq.impl.DSL.val;

@Service
public class StudInjDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    public void insertStudInj(StudInj studInj) {
        Result<StudInjRecord> result = context.insertInto(STUD_INJ)
                .columns(STUD_INJ.N,
                        STUD_INJ.OP_CREATE,
                        STUD_INJ.STUD_N,
                        STUD_INJ.INJ_ACTIVITY_BQ)
                .values(Sequences.STUD_INJ_N.nextval(),
                        Sequences.SYS_OPERATION_N.nextval(),
                        val(studInj.getStudN()),
                        val(studInj.getInjActivityBq()))
                .returning(STUD_INJ.N)
                .fetch();
        studInj.setN(result.get(0).getN());
    }

    public void deleteStudInj(StudInj stdInjId) {
        int numberOfdeletedRecords = context.deleteFrom(STUD_INJ)
                .where(STUD_INJ.N.eq(stdInjId.getN()))
                .execute();
    }

    public Optional<StudInj> findByStudy(Stud stud) {
        StudInjRecord nbcStudInjRecord = context.fetchOne(STUD_INJ, STUD_INJ.STUD_N.eq(stud.getN()));
        return StudInj.buildFromRecord(nbcStudInjRecord);
    }

    public void updateInj(StudInj studInj) {
        context.update(STUD_INJ)
                .set(STUD_INJ.INJ_ACTIVITY_BQ, studInj.getInjActivityBq())
                .set(STUD_INJ.STUD_N, studInj.getStudN())
                .where(STUD_INJ.N.eq(studInj.getN()))
                .execute();
    }
}
