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

@Service
public class GenesDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    private static Map<Long, Gene> genes;

    @PostConstruct
    void init() {
        genes = Collections.unmodifiableMap(context.fetch(GENES)
                .stream()
                .map(Gene::buildFromRecord)
                .collect(toMap(Gene::getN, identity()))
        );
    }

    public static Map<Long, Gene> getGenes() {
        return genes;
    }
}
