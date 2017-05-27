package lgk.nsbc.model.dao.dictionary;

import lgk.nsbc.model.dictionary.ProcTimeApprox;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static lgk.nsbc.generated.tables.NbcProcTimeApprox.NBC_PROC_TIME_APPROX;

@Service
public class ProcTimeApproxDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    private static Map<Long, ProcTimeApprox> procTimeApproxMap;

    @PostConstruct
    void init() {
        procTimeApproxMap = Collections.unmodifiableMap(context.fetch(NBC_PROC_TIME_APPROX)
                .stream()
                .map(ProcTimeApprox::buildFromRecord)
                .collect(toMap(ProcTimeApprox::getN, identity()))
        );
    }

    public static Map<Long, ProcTimeApprox> getProcTimeApproxMap() {
        return procTimeApproxMap;
    }
}