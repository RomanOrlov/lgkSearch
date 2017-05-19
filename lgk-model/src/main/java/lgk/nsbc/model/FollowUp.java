package lgk.nsbc.model;

import lombok.*;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.NbcFollowup.NBC_FOLLOWUP;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowUp implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private Long nbc_target_n;
    private Long nbc_stud_n;
    private Double volume;

    public static FollowUp buildFromRecord(Record record) {
        return builder().n(record.get(NBC_FOLLOWUP.N))
                .nbc_stud_n(record.get(NBC_FOLLOWUP.NBC_STUD_N))
                .nbc_target_n(record.get(NBC_FOLLOWUP.NBC_TARGET_N))
                .volume(record.get(NBC_FOLLOWUP.VOLUME))
                .build();
    }
}
