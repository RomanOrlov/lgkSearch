package lgk.nsbc.model.dao.dictionary;

import lgk.nsbc.model.dictionary.Gene;
import lgk.nsbc.model.dictionary.MutationType;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static lgk.nsbc.generated.tables.Genes.GENES;
import static lgk.nsbc.generated.tables.MutationTypes.MUTATION_TYPES;
import static lgk.nsbc.model.dao.dictionary.MutationTypesDao.getMutationTypes;
import static lgk.nsbc.model.dao.dictionary.MutationTypesDao.mutationTypes;

@Service
public class GenesDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    private static Map<Long, Gene> genes;

    // key - gene Id value, - mutation id
    private static Map<Long, Long> genesMutations = new HashMap<>();

    static {
        genesMutations.put(1L, 2L);
        genesMutations.put(2L, 1L);
        genesMutations.put(3L, 1L);
        genesMutations.put(4L, 1L);
        genesMutations.put(5L, 1L);
        genesMutations.put(6L, 1L);
        genesMutations.put(7L, 1L);
        genesMutations.put(8L, 6L);
        genesMutations.put(9L, 4L);
        genesMutations.put(10L, 1L);
        genesMutations.put(11L, 1L);
    }

    @PostConstruct
    void init() {
        // Важен порядок. Нам сначала нужны типы мутаций, а потом можем приписать гену определенный тип мутации.
        mutationTypes = Collections.unmodifiableMap(context.fetch(MUTATION_TYPES)
                .stream()
                .map(MutationType::buildFromRecord)
                .collect(toMap(MutationType::getN, identity()))
        );
        genes = Collections.unmodifiableMap(context.fetch(GENES)
                .stream()
                .map(Gene::buildFromRecord)
                .peek(gene -> gene.setMutationType(mutationTypes.get(genesMutations.get(gene.getN()))))
                .collect(toMap(Gene::getN, identity()))
        );
    }

    public static Map<Long, Gene> getGenes() {
        return genes;
    }

    public static Map<Long, Long> getGenesMutations() {
        return genesMutations;
    }
}
