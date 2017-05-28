package lgk.nsbc.model.dictionary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.HistologyDiagVerif.HISTOLOGY_DIAG_VERIF;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiagVerif implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private String name;
    private String text;

    public static DiagVerif buildFromRecord(Record record) {
        return builder().n(record.get(HISTOLOGY_DIAG_VERIF.N))
                .name(record.get(HISTOLOGY_DIAG_VERIF.NAME))
                .text(record.get(HISTOLOGY_DIAG_VERIF.TEXT))
                .build();
    }
}
