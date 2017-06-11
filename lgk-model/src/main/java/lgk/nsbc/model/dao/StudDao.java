package lgk.nsbc.model.dao;

import lgk.nsbc.generated.Sequences;
import lgk.nsbc.generated.tables.records.StudRecord;
import lgk.nsbc.model.DateUtils;
import lgk.nsbc.model.Patients;
import lgk.nsbc.model.Stud;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static java.util.stream.Collectors.toList;
import static lgk.nsbc.generated.tables.Stud.STUD;
import static org.jooq.impl.DSL.val;

@Service
public class StudDao implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final long ANAMNEZ_STUD = 9L;
    private Logger logger = Logger.getLogger(StudDao.class.getName());
    @Autowired
    private DSLContext context;

    public boolean isSpectStudyExist(Long patientsId, Date studyDate) {
        Timestamp timestamp = new Timestamp(studyDate.getTime());
        return context.fetchExists(STUD, STUD.PATIENTS_N.eq(patientsId)
                .and(STUD.STUDYDATETIME.eq(timestamp))
                .and(STUD.STUDY_TYPE.eq(11L)));
    }

    public boolean isHistologyStudyExist(Long patientsId, Date studyDate) {
        Timestamp timestamp = new Timestamp(studyDate.getTime());
        return context.fetchExists(STUD, STUD.PATIENTS_N.eq(patientsId)
                .and(STUD.STUDYDATETIME.eq(timestamp))
                .and(STUD.STUDY_TYPE.eq(8L)));
    }

    public void createStud(Stud stud) {
        Timestamp timestamp = DateUtils.fromDateToTimestamp(stud.getStudyDateTime());
        Result<StudRecord> result = context.insertInto(STUD)
                .columns(STUD.N,
                        STUD.OP_CREATE,
                        STUD.STUDYDATETIME,
                        STUD.PATIENTS_N,
                        STUD.STUDY_TYPE
                )
                .values(Sequences.STUD_N.nextval(),
                        Sequences.SYS_OPERATION_N.nextval(),
                        val(timestamp),
                        val(stud.getPatientsN()),
                        val(stud.getStudType().getN())
                )
                .returning(STUD.N)
                .fetch();
        Long generatedId = result.get(0).getN();
        stud.setN(generatedId);
    }

    public List<Stud> findPatientsStuds(Patients patients) {
        Result<StudRecord> result = context.fetch(STUD, STUD.PATIENTS_N.eq(patients.getN()));
        return result.stream()
                .map(Stud::buildFromRecord)
                .map(Optional::get)
                .collect(toList());
    }

    public List<Stud> findPatientsStuds(Patients patients, Long studyType) {
        Result<StudRecord> result = context.fetch(STUD, STUD.PATIENTS_N.eq(patients.getN()).and(STUD.STUDY_TYPE.eq(studyType)));
        return result.stream()
                .map(Stud::buildFromRecord)
                .map(Optional::get)
                .collect(toList());
    }

    public List<Stud> findPatientsStuds(Collection<Long> patientsId) {
        return context.fetch(STUD, STUD.PATIENTS_N.in(patientsId))
                .stream()
                .map(Stud::buildFromRecord)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    public Optional<Stud> findStudyByDate(Patients patients, Date studyDate, Long studyType) {
        Timestamp timestamp = new Timestamp(studyDate.getTime());
        Result<StudRecord> result = context.fetch(STUD, STUD.PATIENTS_N.eq(patients.getN())
                .and(STUD.STUDYDATETIME.eq(timestamp))
                .and(STUD.STUDY_TYPE.eq(studyType)));
        if (result.isEmpty())
            return Optional.empty();
        return Stud.buildFromRecord(result.get(0));
    }

    public Optional<Stud> findById(Long id) {
        StudRecord nbcStudRecord = context.fetchOne(STUD, STUD.N.eq(id));
        return Stud.buildFromRecord(nbcStudRecord);
    }

    public List<Stud> findById(List<Long> studId) {
        Result<StudRecord> result = context.fetch(STUD, STUD.N.in(studId));
        return result.stream()
                .map(Stud::buildFromRecord)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    public void deleteStudy(Stud stud) {
        int removedRecords = context.deleteFrom(STUD)
                .where(STUD.N.eq(stud.getN()))
                .execute();
    }

    public boolean isPatientHasSpectStudy(Patients patients) {
        return context.fetchExists(STUD, STUD.PATIENTS_N.eq(patients.getN())
                .and(STUD.STUDY_TYPE.eq(11L)));
    }

    public void updateStudy(Stud stud) {
        Timestamp timestamp = DateUtils.fromDateToTimestamp(stud.getStudyDateTime());
        context.update(STUD)
                .set(STUD.STUDYDATETIME, timestamp)
                .set(STUD.PROCEDURES_N, stud.getProceduresN())
                .where(STUD.N.eq(stud.getN()))
                .execute();
    }
}
