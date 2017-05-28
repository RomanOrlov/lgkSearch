package lgk.nsbc.model.dao;

import lgk.nsbc.generated.Sequences;
import lgk.nsbc.generated.tables.records.SmplPatientsRecord;
import lgk.nsbc.model.SamplePatients;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import static lgk.nsbc.generated.tables.SmplPatients.SMPL_PATIENTS;
import static org.jooq.impl.DSL.val;

@Service
public class SamplePatientsDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    public List<SamplePatients> findSamplePatients(Long sampleId) {
        return context.fetch(SMPL_PATIENTS, SMPL_PATIENTS.SAMPLES_N.eq(sampleId))
                .stream()
                .map(SamplePatients::buildFromRecord)
                .collect(Collectors.toList());
    }

    public void updateSamplePatient(SamplePatients samplePatients) {
        context.update(SMPL_PATIENTS)
                .set(SMPL_PATIENTS.INCLUSION, samplePatients.getInclusion())
                .where(SMPL_PATIENTS.N.eq(samplePatients.getN())
                        .and(SMPL_PATIENTS.SAMPLES_N.eq(samplePatients.getSampleId())))
                .execute();
    }

    public boolean isSpectSampleContainsPatient(Long patientId) {
        return context.fetchExists(SMPL_PATIENTS, SMPL_PATIENTS.PATIENTS_N.eq(patientId));
    }

    public void removeSamplePatient(SamplePatients samplePatients) {
        context.deleteFrom(SMPL_PATIENTS)
                .where(SMPL_PATIENTS.N.eq(samplePatients.getN()))
                .execute();
    }

    public void saveSamplePatients(SamplePatients samplePatients) {
        SmplPatientsRecord patientsRecord = context.insertInto(SMPL_PATIENTS)
                .columns(SMPL_PATIENTS.N,
                        SMPL_PATIENTS.OP_CREATE,
                        SMPL_PATIENTS.SAMPLES_N,
                        SMPL_PATIENTS.PATIENTS_N,
                        SMPL_PATIENTS.COMMENTS,
                        SMPL_PATIENTS.INCLUSION)
                .values(Sequences.SMPL_PATIENTS_N.nextval(),
                        Sequences.SYS_OPERATION_N.nextval(),
                        val(samplePatients.getSampleId()),
                        val(samplePatients.getPatientId()),
                        val(samplePatients.getComment()),
                        val(samplePatients.getInclusion()))
                .returning(SMPL_PATIENTS.N)
                .fetchOne();
        samplePatients.setN(patientsRecord.getN());
    }
}
