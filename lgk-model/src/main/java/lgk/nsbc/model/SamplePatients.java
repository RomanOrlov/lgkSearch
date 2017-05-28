package lgk.nsbc.model;

import lombok.*;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.SmplPatients.SMPL_PATIENTS;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SamplePatients implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private Long sampleId;
    private Long patientId;
    private String comment;
    private String inclusion;

    public static SamplePatients buildFromRecord(Record record) {
        return builder().n(record.get(SMPL_PATIENTS.N))
                .sampleId(record.get(SMPL_PATIENTS.SAMPLES_N))
                .patientId(record.get(SMPL_PATIENTS.PATIENTS_N))
                .comment(record.get(SMPL_PATIENTS.COMMENTS))
                .inclusion(record.get(SMPL_PATIENTS.INCLUSION))
                .build();
    }
}
