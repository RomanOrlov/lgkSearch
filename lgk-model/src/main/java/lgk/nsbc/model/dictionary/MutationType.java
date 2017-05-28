package lgk.nsbc.model.dictionary;

import lombok.*;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.MutationTypes.MUTATION_TYPES;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"n"})
public class MutationType implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private String name;
    private String text;
    private String description;

    public static MutationType buildFromRecord(Record record) {
        return builder().n(record.get(MUTATION_TYPES.N))
                .name(record.get(MUTATION_TYPES.NAME))
                .text(record.get(MUTATION_TYPES.TEXT))
                .description(record.get(MUTATION_TYPES.DESCRIPTION))
                .build();
    }

    @Override
    public String toString() {
        return text;
    }
}
