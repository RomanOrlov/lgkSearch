package lgk.nsbc.model.dao.dictionary;

import lgk.nsbc.model.dictionary.Organizations;
import lombok.Getter;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static lgk.nsbc.generated.tables.NbcOrganizations.NBC_ORGANIZATIONS;

@Service
public class OrganizationsDao {
    @Autowired
    private DSLContext context;

    @Getter
    private Map<Long, Organizations> organizationsMap;

    @PostConstruct
    void init() {
        organizationsMap = Collections.unmodifiableMap(context.fetch(NBC_ORGANIZATIONS)
                .stream()
                .map(Organizations::buildFromRecord)
                .collect(toMap(Organizations::getN, identity()))
        );
    }
}
