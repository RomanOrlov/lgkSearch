package lgk.nsbc.model.dictionary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jooq.Record;

import static lgk.nsbc.generated.tables.NbcHistology_1DiagVerif.NBC_HISTOLOGY_1_DIAG_VERIF;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiagVerif {
    private Long n;
    private String name;
    private String text;

    public static DiagVerif buildFromRecord(Record record) {
        return builder().n(record.get(NBC_HISTOLOGY_1_DIAG_VERIF.N))
                .name(record.get(NBC_HISTOLOGY_1_DIAG_VERIF.NAME))
                .text(record.get(NBC_HISTOLOGY_1_DIAG_VERIF.TEXT))
                .build();
    }
}
