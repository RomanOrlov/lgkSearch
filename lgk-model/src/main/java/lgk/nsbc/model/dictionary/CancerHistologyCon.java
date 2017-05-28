package lgk.nsbc.model.dictionary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.CancerHistologyCon.CANCER_HISTOLOGY_CON;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CancerHistologyCon implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private String name;
    private String text;

    public static CancerHistologyCon buildFromRecord(Record record) {
        return builder().n(record.get(CANCER_HISTOLOGY_CON.N))
                .name(record.get(CANCER_HISTOLOGY_CON.NAME))
                .text(record.get(CANCER_HISTOLOGY_CON.TEXT))
                .build();
    }
}
