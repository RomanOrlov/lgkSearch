package lgk.nsbc.model.dao;

import lgk.nsbc.model.SamplePatients;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import static lgk.nsbc.generated.tables.NbcSmplPatients.NBC_SMPL_PATIENTS;

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
                .where(NBC_SMPL_PATIENTS.N.eq(samplePatients.getN()));
    }
}
