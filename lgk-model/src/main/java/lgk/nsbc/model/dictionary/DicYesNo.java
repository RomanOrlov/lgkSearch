package lgk.nsbc.model.dictionary;

import lombok.*;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.DicYesNo.DIC_YES_NO;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"n"})
public class DicYesNo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private String name;
    private String text;

    public static DicYesNo buildFromRecord(Record record) {
        return builder().n(record.get(DIC_YES_NO.N))
                .name(record.get(DIC_YES_NO.NAME))
                .text(record.get(DIC_YES_NO.TEXT))
                .build();
    }

    @Override
    public String toString() {
        return text;
    }
}
