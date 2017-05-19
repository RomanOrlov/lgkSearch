package lgk.nsbc.model.dao;

import lgk.nsbc.generated.Sequences;
import lgk.nsbc.generated.tables.records.NbcStudInjRecord;
import lgk.nsbc.model.Stud;
import lgk.nsbc.model.StudInj;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Optional;

import static lgk.nsbc.generated.tables.NbcStudInj.NBC_STUD_INJ;
import static org.jooq.impl.DSL.val;

@Service
public class StudInjDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    public void insertStudInj(StudInj studInj) {
        Result<NbcStudInjRecord> result = context.insertInto(NBC_STUD_INJ)
                .columns(NBC_STUD_INJ.N,
                        NBC_STUD_INJ.OP_CREATE,
                        NBC_STUD_INJ.NBC_STUD_N,
                        NBC_STUD_INJ.INJ_ACTIVITY_BQ)
                .values(Sequences.NBC_STUD_INJ_N.nextval(),
                        Sequences.SYS_OPERATION_N.nextval(),
                        val(studInj.getNbc_stud_n()),
                        val(studInj.getInj_activity_bq()))
                .returning(NBC_STUD_INJ.N)
                .fetch();
        studInj.setN(result.get(0).getN());
    }

    public void deleteStudInj(StudInj stdInjId) {
        int numberOfdeletedRecords = context.deleteFrom(NBC_STUD_INJ)
                .where(NBC_STUD_INJ.N.eq(stdInjId.getN()))
                .execute();
    }

    public Optional<StudInj> findByStudy(Stud stud) {
        NbcStudInjRecord nbcStudInjRecord = context.fetchOne(NBC_STUD_INJ, NBC_STUD_INJ.NBC_STUD_N.eq(stud.getN()));
        return StudInj.buildFromRecord(nbcStudInjRecord);
    }

    public void updateInj(StudInj studInj) {
        context.update(NBC_STUD_INJ)
                .set(NBC_STUD_INJ.INJ_ACTIVITY_BQ, studInj.getInj_activity_bq())
                .set(NBC_STUD_INJ.NBC_STUD_N, studInj.getNbc_stud_n())
                .where(NBC_STUD_INJ.N.eq(studInj.getN()))
                .execute();
    }
}
