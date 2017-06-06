package lgk.nsbc.model.dictionary;

import lgk.nsbc.generated.tables.FollowupEdema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.FollowupEdema.FOLLOWUP_EDEMA;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowUpEdema implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private String name;
    private String text;

    public static FollowUpEdema buildFromRecord(Record record) {
        return builder().n(record.get(FOLLOWUP_EDEMA.N))
                .name(record.get(FOLLOWUP_EDEMA.NAME))
                .text(record.get(FOLLOWUP_EDEMA.TEXT))
                .build();
    }

    @Override
    public String toString() {
        return text;
    }
}
