package lgk.nsbc.model.dao.histology;

import lgk.nsbc.generated.tables.records.NbcHistology_1MutationRecord;
import lgk.nsbc.model.histology.Histology;
import lgk.nsbc.model.histology.Mutation;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static lgk.nsbc.generated.tables.NbcHistology_1Mutation.NBC_HISTOLOGY_1_MUTATION;

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
}
