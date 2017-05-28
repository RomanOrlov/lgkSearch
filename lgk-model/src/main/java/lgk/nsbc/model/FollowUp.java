package lgk.nsbc.model;

import lombok.*;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.Followup.FOLLOWUP;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowUp implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private Long targetN;
    private Long studN;
    private Double volume;

    public static FollowUp buildFromRecord(Record record) {
        return builder().n(record.get(FOLLOWUP.N))
                .studN(record.get(FOLLOWUP.STUD_N))
                .targetN(record.get(FOLLOWUP.TARGET_N))
                .volume(record.get(FOLLOWUP.VOLUME))
                .build();
    }
}
