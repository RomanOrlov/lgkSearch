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
                .histologyN(record.get(NBC_HISTOLOGY_1_MUTATION.NBC_HISTOLOGY_1_N))
                .studyN(record.get(NBC_HISTOLOGY_1_MUTATION.NBC_STUD_N))
                .mutationTypeN(record.get(NBC_HISTOLOGY_1_MUTATION.NBC_MUTATION_TYPES_N))
                .geneN(record.get(NBC_HISTOLOGY_1_MUTATION.NBC_GENES_N))
                .yesNoN(record.get(NBC_HISTOLOGY_1_MUTATION.NBC_DIC_YES_NO_N))
                .build();
    }
}