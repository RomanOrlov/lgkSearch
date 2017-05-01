package lgk.nsbc.model;

import lombok.*;
import org.jooq.Record;

import static lgk.nsbc.generated.tables.NbcFollowup.NBC_FOLLOWUP;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NbcFollowUp {
    private Long n;
    private Long nbc_target_n;
    private Long nbc_stud_n;
    private Double volume;

    public static NbcFollowUp buildFromRecord(Record record) {
        return builder().n(record.get(NBC_FOLLOWUP.N))
                .nbc_stud_n(record.get(NBC_FOLLOWUP.NBC_STUD_N))
                .nbc_target_n(record.get(NBC_FOLLOWUP.NBC_TARGET_N))
                .volume(record.get(NBC_FOLLOWUP.VOLUME))
                .build();
    }
}
