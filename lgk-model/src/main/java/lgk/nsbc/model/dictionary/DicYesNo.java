package lgk.nsbc.model.dictionary;

import lombok.*;
import org.jooq.Record;

import static lgk.nsbc.generated.tables.NbcDicYesNo.NBC_DIC_YES_NO;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DicYesNo {
    private Long n;
    private String name;
    private String text;

    public static DicYesNo buildFromRecord(Record record) {
        return builder().n(record.get(NBC_DIC_YES_NO.N))
                .name(record.get(NBC_DIC_YES_NO.NAME))
                .text(record.get(NBC_DIC_YES_NO.TEXT))
                .build();
    }

    @Override
    public String toString() {
        return text;
    }
}
