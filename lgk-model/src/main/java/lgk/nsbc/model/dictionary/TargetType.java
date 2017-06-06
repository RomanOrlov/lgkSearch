package lgk.nsbc.model.dictionary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.TargetTargettype.TARGET_TARGETTYPE;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TargetType implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private String name;
    private String text;
    public Integer listOrder;

    public static TargetType buildFromRecord(Record record) {
        return builder().n(record.get(TARGET_TARGETTYPE.N))
                .name(record.get(TARGET_TARGETTYPE.NAME))
                .text(record.get(TARGET_TARGETTYPE.TEXT))
                .listOrder(record.get(TARGET_TARGETTYPE.LIST_ORDER))
                .build();
    }

    @Override
    public String toString() {
        return text;
    }
}
