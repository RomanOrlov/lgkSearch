package lgk.nsbc.model.dao;

import lgk.nsbc.generated.Sequences;
import lgk.nsbc.generated.tables.records.NbcSmplPatientsRecord;
import lgk.nsbc.model.SamplePatients;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import static lgk.nsbc.generated.tables.NbcSmplPatients.NBC_SMPL_PATIENTS;
import static org.jooq.impl.DSL.val;

@Service
public class SamplePatientsDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    public List<SamplePatients> findSamplePatients(Long sampleId) {
        return context.fetch(NBC_SMPL_PATIENTS, NBC_SMPL_PATIENTS.BAS_SAMPLES_N.eq(sampleId))
                .stream()
                .map(SamplePatients::buildFromRecord)
                .collect(Collectors.toList());
    }

    public void updateSamplePatient(SamplePatients samplePatients) {
        context.update(NBC_SMPL_PATIENTS)
                .set(NBC_SMPL_PATIENTS.INCLUSION, samplePatients.getInclusion())
                .where(NBC_SMPL_PATIENTS.N.eq(samplePatients.getN())
                        .and(NBC_SMPL_PATIENTS.BAS_SAMPLES_N.eq(samplePatients.getSampleId())))
                .execute();
    }

    public boolean isSpectSampleContainsPatient(Long patientId) {
        return context.fetchExists(NBC_SMPL_PATIENTS, NBC_SMPL_PATIENTS.NBC_PATIENTS_N.eq(patientId));
    }

    public void removeSamplePatient(SamplePatients samplePatients) {
        context.deleteFrom(NBC_SMPL_PATIENTS)
                .where(NBC_SMPL_PATIENTS.N.eq(samplePatients.getN()))
                .execute();
    }

    public void saveSamplePatients(SamplePatients samplePatients) {
        NbcSmplPatientsRecord patientsRecord = context.insertInto(NBC_SMPL_PATIENTS)
                .columns(NBC_SMPL_PATIENTS.N,
                        NBC_SMPL_PATIENTS.OP_CREATE,
                        NBC_SMPL_PATIENTS.BAS_SAMPLES_N,
                        NBC_SMPL_PATIENTS.NBC_PATIENTS_N,
                        NBC_SMPL_PATIENTS.COMMENTS,
                        NBC_SMPL_PATIENTS.INCLUSION)
                .values(Sequences.NBC_SMPL_PATIENTS_N.nextval(),
                        Sequences.SYS_OPERATION_N.nextval(),
                        val(samplePatients.getSampleId()),
                        val(samplePatients.getPatientId()),
                        val(samplePatients.getComment()),
                        val(samplePatients.getInclusion()))
                .returning(NBC_SMPL_PATIENTS.N)
                .fetchOne();
        samplePatients.setN(patientsRecord.getN());
    }
}
