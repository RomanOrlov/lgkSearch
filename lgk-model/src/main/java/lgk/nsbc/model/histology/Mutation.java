package lgk.nsbc.model.histology;

import lgk.nsbc.model.dao.dictionary.DicYesNoDao;
import lgk.nsbc.model.dictionary.DicYesNo;
import lgk.nsbc.model.dictionary.Gene;
import lgk.nsbc.model.dictionary.MutationType;
import lombok.*;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.NbcHistology_1Mutation.NBC_HISTOLOGY_1_MUTATION;
import static lgk.nsbc.model.dao.dictionary.GenesDao.getGenes;
import static lgk.nsbc.model.dao.dictionary.MutationTypesDao.getMutationTypes;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Mutation implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private Long histologyN;
    private Long studyN;
    private MutationType mutationType;
    private Gene gene;
    private DicYesNo dicYesNo;

    public static Mutation buildFromRecord(Record record) {
        return builder().n(record.get(NBC_HISTOLOGY_1_MUTATION.N))
                .histologyN(record.get(NBC_HISTOLOGY_1_MUTATION.NBC_HISTOLOGY_1_N))
                .studyN(record.get(NBC_HISTOLOGY_1_MUTATION.NBC_STUD_N))
                .mutationType(getMutationTypes().get(record.get(NBC_HISTOLOGY_1_MUTATION.NBC_MUTATION_TYPES_N)))
                .gene(getGenes().get(record.get(NBC_HISTOLOGY_1_MUTATION.NBC_GENES_N)))
                .dicYesNo(DicYesNoDao.getDicYesNo().get(record.get(NBC_HISTOLOGY_1_MUTATION.NBC_DIC_YES_NO_N)))
                .build();
    }
}