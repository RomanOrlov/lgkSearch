package lgk.nsbc.model.dictionary;

import lombok.*;
import org.jooq.Record;

import static lgk.nsbc.generated.tables.NbcGenes.NBC_GENES;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Genes {
    private Long n;
    private String name;
    private String text;
    private String description;

    public static Genes buildFromRecord(Record record) {
        return builder().n(record.get(NBC_GENES.N))
                .name(record.get(NBC_GENES.NAME))
                .text(record.get(NBC_GENES.TEXT))
                .description(record.get(NBC_GENES.DESCRIPTION))
                .build();
    }

    @Override
    public String toString() {
        return name;
    }
}
