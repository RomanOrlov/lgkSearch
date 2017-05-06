package lgk.nsbc.model.dao.dictionary;

import lgk.nsbc.model.dictionary.Genes;
import lombok.Getter;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static lgk.nsbc.generated.tables.NbcGenes.NBC_GENES;

@Service
public class GenesDao {
    @Autowired
    private DSLContext context;

    @Getter
    private Map<Long, Genes> genes;

    @PostConstruct
    void init() {
        genes = Collections.unmodifiableMap(context.fetch(NBC_GENES)
                .stream()
                .map(Genes::buildFromRecord)
                .collect(toMap(Genes::getN, identity()))
        );
    }
}
