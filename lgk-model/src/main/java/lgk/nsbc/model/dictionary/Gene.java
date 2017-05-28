package lgk.nsbc.model.dictionary;

import lombok.*;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.Genes.GENES;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"n"})
public class Gene implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private String name;
    private String text;
    private String description;

    public static Gene buildFromRecord(Record record) {
        return builder().n(record.get(GENES.N))
                .name(record.get(GENES.NAME))
                .text(record.get(GENES.TEXT))
                .description(record.get(GENES.DESCRIPTION))
                .build();
    }

    @Override
    public String toString() {
        return name;
    }
}
