package lgk.nsbc.model.dao.dictionary;

import lgk.nsbc.model.dictionary.ProcRtTech;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static lgk.nsbc.generated.tables.NbcProcRtTech.NBC_PROC_RT_TECH;

@Service
public class ProcRtTechDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    private static Map<Long, ProcRtTech> procRtTechMap;

    @PostConstruct
    void init() {
        procRtTechMap = Collections.unmodifiableMap(context.fetch(NBC_PROC_RT_TECH)
                .stream()
                .map(ProcRtTech::buildFromRecord)
                .collect(toMap(ProcRtTech::getN, identity()))
        );
    }

    public static Map<Long, ProcRtTech> getProcRtTechMap() {
        return procRtTechMap;
    }
}
