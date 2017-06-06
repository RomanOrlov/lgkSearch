package lgk.nsbc.model.dao.histology;

import lgk.nsbc.generated.Sequences;
import lgk.nsbc.generated.tables.records.HistologyMutationRecord;
import lgk.nsbc.model.histology.Histology;
import lgk.nsbc.model.histology.Mutation;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep7;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static lgk.nsbc.generated.Sequences.HISTOLOGY_MUTATION_N;
import static lgk.nsbc.generated.tables.HistologyMutation.HISTOLOGY_MUTATION;
import static org.jooq.impl.DSL.val;

@Service
public class MutationsDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    public Map<Long, List<Mutation>> findMutationsByHistologyList(List<Histology> histologyList) {
        Set<Long> histologyIdSet = histologyList.stream()
                .map(Histology::getN)
                .collect(Collectors.toSet());
        Result<HistologyMutationRecord> result = context.fetch(HISTOLOGY_MUTATION, HISTOLOGY_MUTATION.HISTOLOGY_N.in(histologyIdSet));
        return result.stream()
                .map(Mutation::buildFromRecord)
                .collect(toMap(Mutation::getHistologyN, Arrays::asList, (m1, m2) -> {
                    List<Mutation> temp = new ArrayList<>(m1);
                    temp.addAll(m2);
                    return temp;
                }));
    }

    public List<Mutation> findMutationByHistology(Histology histology) {
        Result<HistologyMutationRecord> result = context.fetch(HISTOLOGY_MUTATION, HISTOLOGY_MUTATION.HISTOLOGY_N.eq(histology.getN()));
        return result.stream()
                .map(Mutation::buildFromRecord)
                .collect(toList());
    }

    public void deleteMutationsByHistology(Histology histology) {
        int deletedRecords = context.deleteFrom(HISTOLOGY_MUTATION)
                .where(HISTOLOGY_MUTATION.HISTOLOGY_N.eq(histology.getN()))
                .execute();
    }

    public void saveMutations(Collection<Mutation> mutations) {
        InsertValuesStep7<HistologyMutationRecord, Long, Long, Long, Long, Long, Long, Long> columns = context.insertInto(HISTOLOGY_MUTATION)
                .columns(HISTOLOGY_MUTATION.N,
                        HISTOLOGY_MUTATION.OP_CREATE,
                        HISTOLOGY_MUTATION.HISTOLOGY_N,
                        HISTOLOGY_MUTATION.STUD_N,
                        HISTOLOGY_MUTATION.DIC_YES_NO_N,
                        HISTOLOGY_MUTATION.GENES_N,
                        HISTOLOGY_MUTATION.MUTATION_TYPES_N);
        for (Mutation mutation : mutations) {
            columns = columns.values(HISTOLOGY_MUTATION_N.nextval(),
                    Sequences.SYS_OPERATION_N.nextval(),
                    val(mutation.getHistologyN()),
                    val(mutation.getStudyN()),
                    val(mutation.getDicYesNo() == null ? null : mutation.getDicYesNo().getN()),
                    val(mutation.getGene() == null ? null : mutation.getGene().getN()),
                    val(mutation.getGene() == null ? null : mutation.getGene().getMutationType().getN()));
        }
        columns.execute();
    }
}
