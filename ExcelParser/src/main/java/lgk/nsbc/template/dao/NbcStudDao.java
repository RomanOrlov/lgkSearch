package lgk.nsbc.template.dao;

import lgk.nsbc.generated.Sequences;
import lgk.nsbc.generated.tables.records.NbcStudRecord;
import lgk.nsbc.template.model.NbcPatients;
import lgk.nsbc.template.model.NbcStud;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Logger;

import static lgk.nsbc.generated.tables.NbcStud.NBC_STUD;
import static org.jooq.impl.DSL.val;

@Service
public class NbcStudDao {
    private Logger logger = Logger.getLogger(NbcStudDao.class.getName());
    @Autowired
    private DSLContext context;

    public boolean isSpectStudyExist(NbcStud nbcStud) {
        Timestamp timestamp = new Timestamp(nbcStud.getStudydatetime().getTime());
        return context.fetchExists(NBC_STUD, NBC_STUD.NBC_PATIENTS_N.eq(nbcStud.getNbc_patients_n())
                .and(NBC_STUD.STUDYDATETIME.eq(timestamp))
                .and(NBC_STUD.STUDY_TYPE.eq(11L)));
    }

    // Должен быть в транзакции см управление транзакциями в джуке
    public void createNbcStud(NbcStud nbcStud) {
        Timestamp timestamp = new Timestamp(nbcStud.getStudydatetime().getTime());
        Result<NbcStudRecord> result = context.insertInto(NBC_STUD)
                .columns(NBC_STUD.N,
                        NBC_STUD.OP_CREATE,
                        NBC_STUD.STUDYDATETIME,
                        NBC_STUD.NBC_PATIENTS_N,
                        NBC_STUD.STUDY_TYPE
                )
                .values(Sequences.NBC_STUD_N.nextval(),
                        Sequences.SYS_OPERATION_N.nextval(),
                        val(timestamp),
                        val(nbcStud.getNbc_patients_n()),
                        val(nbcStud.getStudy_type())
                )
                .returning(NBC_STUD.N)
                .fetch();
        Long generatedId = result.get(0).getN();
        nbcStud.setN(generatedId);
    }
}
