package lgk.nsbc.model.dao.dictionary;

import lgk.nsbc.model.dictionary.TargetType;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static lgk.nsbc.generated.tables.TargetTargettype.TARGET_TARGETTYPE;

@Service
public class TargetTypeDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    private static Map<Long, TargetType> targetTypeMap;

    @PostConstruct
    void init() {
        targetTypeMap = Collections.unmodifiableMap(context.fetch(TARGET_TARGETTYPE)
                .stream()
                .map(TargetType::buildFromRecord)
                .collect(toMap(TargetType::getN, identity()))
        );
    }

    public static Map<Long, TargetType> getTargetTypeMap() {
        return targetTypeMap;
    }
}
