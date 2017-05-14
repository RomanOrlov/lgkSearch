package lgk.nsbc.model.dao.histology;

import lgk.nsbc.generated.Sequences;
import lgk.nsbc.generated.tables.NbcHistology_1Mutation;
import lgk.nsbc.generated.tables.records.NbcHistology_1MutationRecord;
import lgk.nsbc.model.histology.Histology;
import lgk.nsbc.model.histology.Mutation;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep6;
import org.jooq.InsertValuesStep7;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static lgk.nsbc.generated.Sequences.NBC_HISTOLOGY_1_MUTATION_N;
import static lgk.nsbc.generated.tables.NbcHistology_1Mutation.NBC_HISTOLOGY_1_MUTATION;
import static org.jooq.impl.DSL.val;

@Service
public class MutationsDao {
    @Autowired
    private DSLContext context;

    public Map<Long, List<Mutation>> findMutationsByHistologyList(List<Histology> histologyList) {
        Set<Long> histologyIdSet = histologyList.stream()
                .map(Histology::getN)
                .collect(Collectors.toSet());
        Result<NbcHistology_1MutationRecord> result = context.fetch(NBC_HISTOLOGY_1_MUTATION, NBC_HISTOLOGY_1_MUTATION.NBC_HISTOLOGY_1_N.in(histologyIdSet));
        return result.stream()
                .map(Mutation::buildFromRecord)
                .collect(toMap(Mutation::getHistologyN, Arrays::asList, (m1, m2) -> {
                    List<Mutation> temp = new ArrayList<>(m1);
                    temp.addAll(m2);
                    return temp;
                }));
    }

    public List<Mutation> findMutationByHistology(Histology histology) {
        Result<NbcHistology_1MutationRecord> result = context.fetch(NBC_HISTOLOGY_1_MUTATION, NBC_HISTOLOGY_1_MUTATION.NBC_HISTOLOGY_1_N.eq(histology.getN()));
        return result.stream()
                .map(Mutation::buildFromRecord)
                .collect(toList());
    }

    public void deleteMutationsByHistology(Histology histology) {
        int deletedRecords = context.deleteFrom(NBC_HISTOLOGY_1_MUTATION)
                .where(NBC_HISTOLOGY_1_MUTATION.NBC_HISTOLOGY_1_N.eq(histology.getN()))
                .execute();
    }

    public void saveMutations(List<Mutation> mutations) {
        InsertValuesStep7<NbcHistology_1MutationRecord, Long, Long, Long, Long, Long, Long, Long> columns = context.insertInto(NBC_HISTOLOGY_1_MUTATION)
                .columns(NBC_HISTOLOGY_1_MUTATION.N,
                        NBC_HISTOLOGY_1_MUTATION.OP_CREATE,
                        NBC_HISTOLOGY_1_MUTATION.NBC_HISTOLOGY_1_N,
                        NBC_HISTOLOGY_1_MUTATION.NBC_STUD_N,
                        NBC_HISTOLOGY_1_MUTATION.NBC_DIC_YES_NO_N,
                        NBC_HISTOLOGY_1_MUTATION.NBC_GENES_N,
                        NBC_HISTOLOGY_1_MUTATION.NBC_MUTATION_TYPES_N);
        for (Mutation mutation : mutations) {
            columns = columns.values(NBC_HISTOLOGY_1_MUTATION_N.nextval(),
                    Sequences.SYS_OPERATION_N.nextval(),
                    val(mutation.getHistologyN()),
                    val(mutation.getStudyN()),
                    val(mutation.getYesNoN()),
                    val(mutation.getGeneN()),
                    val(mutation.getMutationTypeN()));
        }
        columns.execute();
    }
}
