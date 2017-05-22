package lgk.nsbc.model.dictionary;

import lombok.*;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.NbcMutationTypes.NBC_MUTATION_TYPES;

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
        return builder().n(record.get(NBC_MUTATION_TYPES.N))
                .name(record.get(NBC_MUTATION_TYPES.NAME))
                .text(record.get(NBC_MUTATION_TYPES.TEXT))
                .description(record.get(NBC_MUTATION_TYPES.DESCRIPTION))
                .build();
    }

    @Override
    public String toString() {
        return text;
    }
}
