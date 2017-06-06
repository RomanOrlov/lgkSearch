package lgk.nsbc.model.dao.dictionary;

import lgk.nsbc.model.dictionary.FollowUpVisualChange;
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
import static lgk.nsbc.generated.tables.FollowupVisualChange.FOLLOWUP_VISUAL_CHANGE;

@Service
public class FollowUpVisualChangeDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    private static Map<Long, FollowUpVisualChange> followUpVisualChangeMap;

    @PostConstruct
    void init() {
        followUpVisualChangeMap = Collections.unmodifiableMap(context.fetch(FOLLOWUP_VISUAL_CHANGE)
                .stream()
                .map(FollowUpVisualChange::buildFromRecord)
                .collect(toMap(FollowUpVisualChange::getN, identity()))
        );
    }

    public static Map<Long, FollowUpVisualChange> getFollowUpVisualChangeMap() {
        return followUpVisualChangeMap;
    }
}
