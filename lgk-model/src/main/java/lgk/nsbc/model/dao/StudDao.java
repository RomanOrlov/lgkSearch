package lgk.nsbc.model.dao;

import lgk.nsbc.generated.Sequences;
import lgk.nsbc.generated.tables.records.NbcStudRecord;
import lgk.nsbc.model.Patients;
import lgk.nsbc.model.Stud;
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
public class StudDao implements Serializable{
    private Logger logger = Logger.getLogger(StudDao.class.getName());
    @Autowired
    private DSLContext context;

    public boolean isSpectStudyExist(Long patientsId, Date studyDate) {
        Timestamp timestamp = new Timestamp(studyDate.getTime());
        return context.fetchExists(NBC_STUD, NBC_STUD.NBC_PATIENTS_N.eq(patientsId)
                .and(NBC_STUD.STUDYDATETIME.eq(timestamp))
                .and(NBC_STUD.STUDY_TYPE.eq(11L)));
    }

    // Должен быть в транзакции см управление транзакциями в джуке
    public void createNbcStud(Stud stud) {
        Timestamp timestamp = new Timestamp(stud.getStudydatetime().getTime());
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
                        val(stud.getNbc_patients_n()),
                        val(stud.getStudy_type())
                )
                .returning(NBC_STUD.N)
                .fetch();
        Long generatedId = result.get(0).getN();
        stud.setN(generatedId);
    }

    public List<Stud> findPatientsSpectStudy(Patients patients) {
        Result<NbcStudRecord> result = context.fetch(NBC_STUD, NBC_STUD.NBC_PATIENTS_N.eq(patients.getN())
                .and(NBC_STUD.STUDY_TYPE.eq(11L)));

        return result.stream()
                .map(Stud::buildFromRecord)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public Optional<Stud> findSpectStudyByDate(Patients patients, Date studyDate) {
        Timestamp timestamp = new Timestamp(studyDate.getTime());
        NbcStudRecord result = context.fetchOne(NBC_STUD, NBC_STUD.NBC_PATIENTS_N.eq(patients.getN())
                .and(NBC_STUD.STUDYDATETIME.eq(timestamp))
                .and(NBC_STUD.STUDY_TYPE.eq(11L)));
        return Stud.buildFromRecord(result);
    }

    public Optional<Stud> findById(Long id) {
        NbcStudRecord nbcStudRecord = context.fetchOne(NBC_STUD, NBC_STUD.N.eq(id));
        return Stud.buildFromRecord(nbcStudRecord);
    }

    public void deleteStudy(Stud stud) {
        int removedRecords = context.deleteFrom(NBC_STUD)
                .where(NBC_STUD.N.eq(stud.getN()))
                .execute();
    }

    public boolean isPatientHasSpectStudy(Patients patients) {
        return context.fetchExists(NBC_STUD, NBC_STUD.NBC_PATIENTS_N.eq(patients.getN())
                .and(NBC_STUD.STUDY_TYPE.eq(11L)));
    }

    public void updateStudy(Stud stud) {
        Timestamp timestamp = new Timestamp(stud.getStudydatetime().getTime());
        context.update(NBC_STUD)
                .set(NBC_STUD.STUDYDATETIME, timestamp)
                .where(NBC_STUD.N.eq(stud.getN()))
                .execute();
    }
}