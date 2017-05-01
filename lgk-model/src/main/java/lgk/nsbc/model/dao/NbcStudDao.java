package lgk.nsbc.model.dao;

import lgk.nsbc.generated.Sequences;
import lgk.nsbc.generated.tables.records.NbcStudRecord;
import lgk.nsbc.model.NbcPatients;
import lgk.nsbc.model.NbcStud;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static lgk.nsbc.generated.tables.NbcStud.NBC_STUD;
import static org.jooq.impl.DSL.val;

@Service
public class NbcStudDao implements Serializable{
    private Logger logger = Logger.getLogger(NbcStudDao.class.getName());
    @Autowired
    private DSLContext context;

    public boolean isSpectStudyExist(Long patientsId, Date studyDate) {
        Timestamp timestamp = new Timestamp(studyDate.getTime());
        return context.fetchExists(NBC_STUD, NBC_STUD.NBC_PATIENTS_N.eq(patientsId)
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

    public List<NbcStud> findPatientsSpectStudy(NbcPatients nbcPatients) {
        Result<NbcStudRecord> result = context.fetch(NBC_STUD, NBC_STUD.NBC_PATIENTS_N.eq(nbcPatients.getN())
                .and(NBC_STUD.STUDY_TYPE.eq(11L)));

        return result.stream()
                .map(NbcStud::buildFromRecord)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public Optional<NbcStud> findSpectStudyByDate(NbcPatients nbcPatients, Date studyDate) {
        Timestamp timestamp = new Timestamp(studyDate.getTime());
        NbcStudRecord result = context.fetchOne(NBC_STUD, NBC_STUD.NBC_PATIENTS_N.eq(nbcPatients.getN())
                .and(NBC_STUD.STUDYDATETIME.eq(timestamp))
                .and(NBC_STUD.STUDY_TYPE.eq(11L)));
        return NbcStud.buildFromRecord(result);
    }

    public Optional<NbcStud> findById(Long id) {
        NbcStudRecord nbcStudRecord = context.fetchOne(NBC_STUD, NBC_STUD.N.eq(id));
        return NbcStud.buildFromRecord(nbcStudRecord);
    }

    public void deleteStudy(NbcStud nbcStud) {
        int removedRecords = context.deleteFrom(NBC_STUD)
                .where(NBC_STUD.N.eq(nbcStud.getN()))
                .execute();
    }

    public boolean isPatientHasSpectStudy(NbcPatients nbcPatients) {
        return context.fetchExists(NBC_STUD, NBC_STUD.NBC_PATIENTS_N.eq(nbcPatients.getN())
                .and(NBC_STUD.STUDY_TYPE.eq(11L)));
    }

    public void updateStudy(NbcStud nbcStud) {
        Timestamp timestamp = new Timestamp(nbcStud.getStudydatetime().getTime());
        context.update(NBC_STUD)
                .set(NBC_STUD.STUDYDATETIME, timestamp)
                .where(NBC_STUD.N.eq(nbcStud.getN()))
                .execute();
    }
}
