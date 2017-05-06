package lgk.nsbc.model.dao.dictionary;

import lgk.nsbc.model.dictionary.MutationTypes;
import lombok.Getter;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static lgk.nsbc.generated.tables.NbcMutationTypes.NBC_MUTATION_TYPES;

@Service
public class MutationTypesDao {
    @Autowired
    private DSLContext context;

    @Getter
    private Map<Long, MutationTypes> mutationTypes;

    @PostConstruct
    void init() {
        mutationTypes = Collections.unmodifiableMap(context.fetch(NBC_MUTATION_TYPES)
                .stream()
                .map(MutationTypes::buildFromRecord)
                .collect(toMap(MutationTypes::getN, identity()))
        );
    }
}
