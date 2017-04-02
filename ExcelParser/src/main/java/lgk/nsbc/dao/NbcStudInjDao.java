package lgk.nsbc.dao;

import lgk.nsbc.generated.Sequences;
import lgk.nsbc.generated.tables.records.NbcStudInjRecord;
import lgk.nsbc.model.NbcStud;
import lgk.nsbc.model.NbcStudInj;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static lgk.nsbc.generated.tables.NbcStudInj.NBC_STUD_INJ;
import static org.jooq.impl.DSL.val;

@Service
public class NbcStudInjDao {
    @Autowired
    private DSLContext context;

    public void insertStudInj(NbcStudInj nbcStudInj) {
        Result<NbcStudInjRecord> result = context.insertInto(NBC_STUD_INJ)
                .columns(NBC_STUD_INJ.N,
                        NBC_STUD_INJ.OP_CREATE,
                        NBC_STUD_INJ.NBC_STUD_N,
                        NBC_STUD_INJ.INJ_ACTIVITY_BQ)
                .values(Sequences.NBC_STUD_INJ_N.nextval(),
                        Sequences.SYS_OPERATION_N.nextval(),
                        val(nbcStudInj.getNbc_stud_n()),
                        val(nbcStudInj.getInj_activity_bq()))
                .returning(NBC_STUD_INJ.N)
                .fetch();
        nbcStudInj.setN(result.get(0).getN());
    }

    public void deleteStudInj(NbcStudInj stdInjId) {
        int numberOfdeletedRecords = context.deleteFrom(NBC_STUD_INJ)
                .where(NBC_STUD_INJ.N.eq(stdInjId.getN()))
                .execute();
    }

    public NbcStudInj findByStudy(NbcStud nbcStud) {
        NbcStudInjRecord nbcStudInjRecord = context.fetchOne(NBC_STUD_INJ, NBC_STUD_INJ.NBC_STUD_N.eq(nbcStud.getN()));
        return NbcStudInj.buildFromRecord(nbcStudInjRecord);
    }
}
