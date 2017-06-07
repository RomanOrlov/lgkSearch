package lgk.nsbc.model.dao.dictionary;

import lgk.nsbc.model.dictionary.MutationType;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static lgk.nsbc.generated.tables.MutationTypes.MUTATION_TYPES;

@Service
public class MutationTypesDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    static Map<Long, MutationType> mutationTypes;

    @PostConstruct
    public void init() {
            // See GenesDao
    }

    public static Map<Long, MutationType> getMutationTypes() {
        return mutationTypes;
    }
}
