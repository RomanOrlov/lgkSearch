package lgk.nsbc.model.dao.dictionary;

import lgk.nsbc.model.dictionary.FollowUpEdema;
import lombok.Getter;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static lgk.nsbc.generated.tables.FollowupEdema.FOLLOWUP_EDEMA;

@Service
public class FollowUpEdemaDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    private static Map<Long, FollowUpEdema> followUpEdemaMap;

    @PostConstruct
    void init() {
        followUpEdemaMap = Collections.unmodifiableMap(context.fetch(FOLLOWUP_EDEMA)
                .stream()
                .map(FollowUpEdema::buildFromRecord)
                .collect(toMap(FollowUpEdema::getN, identity()))
        );
    }

    public static Map<Long, FollowUpEdema> getFollowUpEdemaMap() {
        return followUpEdemaMap;
    }
}
