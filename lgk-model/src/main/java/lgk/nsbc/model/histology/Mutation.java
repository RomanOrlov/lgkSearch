package lgk.nsbc.model.histology;

import lombok.*;
import org.jooq.Record;

import static lgk.nsbc.generated.tables.NbcHistology_1Mutation.NBC_HISTOLOGY_1_MUTATION;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Mutation {
    private Long n;
    private Long histologyN;
    private Long studyN;
    private Long mutationTypeN;
    private Long geneN;
    private Long yesNoN;

    public static Mutation buildFromRecord(Record record) {
        return builder().n(record.get(NBC_HISTOLOGY_1_MUTATION.N))
                .build();
    }
}