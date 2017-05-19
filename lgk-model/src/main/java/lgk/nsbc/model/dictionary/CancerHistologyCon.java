package lgk.nsbc.model.dictionary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.NbcCancerHistology_1Con.NBC_CANCER_HISTOLOGY_1_CON;

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
        return builder().n(record.get(NBC_CANCER_HISTOLOGY_1_CON.N))
                .name(record.get(NBC_CANCER_HISTOLOGY_1_CON.NAME))
                .text(record.get(NBC_CANCER_HISTOLOGY_1_CON.TEXT))
                .build();
    }
}
