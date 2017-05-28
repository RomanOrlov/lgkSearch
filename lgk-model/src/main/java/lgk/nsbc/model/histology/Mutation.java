package lgk.nsbc.model.histology;

import lgk.nsbc.model.dao.dictionary.DicYesNoDao;
import lgk.nsbc.model.dictionary.DicYesNo;
import lgk.nsbc.model.dictionary.Gene;
import lgk.nsbc.model.dictionary.MutationType;
import lombok.*;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.HistologyMutation.HISTOLOGY_MUTATION;
import static lgk.nsbc.model.dao.dictionary.GenesDao.getGenes;
import static lgk.nsbc.model.dao.dictionary.MutationTypesDao.getMutationTypes;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"gene"})
public class Mutation implements Serializable, Comparable<Mutation> {
    private static final long serialVersionUID = 1L;

    private Long n;
    private Long histologyN;
    private Long studyN;
    private MutationType mutationType;
    private Gene gene;
    private DicYesNo dicYesNo;

    public static Mutation buildFromRecord(Record record) {
        return builder().n(record.get(HISTOLOGY_MUTATION.N))
                .histologyN(record.get(HISTOLOGY_MUTATION.HISTOLOGY_N))
                .studyN(record.get(HISTOLOGY_MUTATION.STUD_N))
                .mutationType(getMutationTypes().get(record.get(HISTOLOGY_MUTATION.MUTATION_TYPES_N)))
                .gene(getGenes().get(record.get(HISTOLOGY_MUTATION.GENES_N)))
                .dicYesNo(DicYesNoDao.getDicYesNo().get(record.get(HISTOLOGY_MUTATION.DIC_YES_NO_N)))
                .build();
    }

    @Override
    public int compareTo(Mutation o) {
        if (gene == null || o.getGene() == null)
            return 0;
        return gene.getName().compareTo(o.getGene().getName());
    }
}