package lgk.nsbc.model.dao.dictionary;

import lgk.nsbc.model.dictionary.Gene;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static lgk.nsbc.generated.tables.Genes.GENES;
import static lgk.nsbc.model.dao.dictionary.MutationTypesDao.getMutationTypes;

@Service
public class GenesDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    private static Map<Long, Gene> genes;

    // key - gene Id value, - mutation id
    private static Map<Long, Long> genesMutations;

    static {
        genesMutations.put(1L, 1L);
        genesMutations.put(2L, 1L);
        genesMutations.put(3L, 1L);
        genesMutations.put(4L, 1L);
        genesMutations.put(5L, 1L);
        genesMutations.put(6L, 1L);
        genesMutations.put(7L, 1L);
        genesMutations.put(8L, 1L);
        genesMutations.put(9L, 1L);
        genesMutations.put(10L, 1L);
        genesMutations.put(11L, 1L);
    }

    @PostConstruct
    void init() {
        genes = Collections.unmodifiableMap(context.fetch(GENES)
                .stream()
                .map(Gene::buildFromRecord)
                .peek(gene -> gene.setMutationType(getMutationTypes().get(genesMutations.get(gene.getN()))))
                .collect(toMap(Gene::getN, identity()))
        );
    }

    public static Map<Long, Gene> getGenes() {
        return genes;
    }
}
