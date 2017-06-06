package lgk.nsbc.model.dictionary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.FollowupVisualChange.FOLLOWUP_VISUAL_CHANGE;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowUpVisualChange implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private String name;
    private String text;

    public static FollowUpVisualChange buildFromRecord(Record record) {
        return builder().n(record.get(FOLLOWUP_VISUAL_CHANGE.N))
                .name(record.get(FOLLOWUP_VISUAL_CHANGE.NAME))
                .text(record.get(FOLLOWUP_VISUAL_CHANGE.TEXT))
                .build();
    }

    @Override
    public String toString() {
        return text;
    }
}
