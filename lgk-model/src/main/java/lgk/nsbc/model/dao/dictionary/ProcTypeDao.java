package lgk.nsbc.model.dao.dictionary;

import lgk.nsbc.model.dictionary.ProcType;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static lgk.nsbc.generated.tables.ProcProcType.PROC_PROC_TYPE;

@Service
public class ProcTypeDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    private static Map<Long, ProcType> procTypeMap;

    @PostConstruct
    void init() {
        procTypeMap = Collections.unmodifiableMap(context.fetch(PROC_PROC_TYPE)
                .stream()
                .map(ProcType::buildFromRecord)
                .collect(toMap(ProcType::getN, identity()))
        );
    }

    public static Map<Long, ProcType> getProcTypeMap() {
        return procTypeMap;
    }
}
