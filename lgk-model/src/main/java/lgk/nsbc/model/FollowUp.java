package lgk.nsbc.model;

import lgk.nsbc.model.dictionary.FollowUpEdema;
import lgk.nsbc.model.dictionary.FollowUpVisualChange;
import lombok.*;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.Followup.FOLLOWUP;
import static lgk.nsbc.model.dao.dictionary.FollowUpEdemaDao.getFollowUpEdemaMap;
import static lgk.nsbc.model.dao.dictionary.FollowUpVisualChangeDao.getFollowUpVisualChangeMap;

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
    private FollowUpEdema edema;
    private FollowUpVisualChange visualChange;

    public static FollowUp buildFromRecord(Record record) {
        return builder().n(record.get(FOLLOWUP.N))
                .studN(record.get(FOLLOWUP.STUD_N))
                .targetN(record.get(FOLLOWUP.TARGET_N))
                .volume(record.get(FOLLOWUP.VOLUME))
                .edema(getFollowUpEdemaMap().get(record.get(FOLLOWUP.EDEMA)))
                .visualChange(getFollowUpVisualChangeMap().get(record.get(FOLLOWUP.VISUAL_CHANGE)))
                .build();
    }
}
