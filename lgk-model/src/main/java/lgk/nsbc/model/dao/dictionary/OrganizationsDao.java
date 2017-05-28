package lgk.nsbc.model.dao.dictionary;

import lgk.nsbc.model.dictionary.Organization;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static lgk.nsbc.generated.tables.Organizations.ORGANIZATIONS;

@Service
public class OrganizationsDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    private static Map<Long, Organization> organizationsMap;

    @PostConstruct
    void init() {
        organizationsMap = Collections.unmodifiableMap(context.fetch(ORGANIZATIONS)
                .stream()
                .map(Organization::buildFromRecord)
                .collect(toMap(Organization::getN, identity()))
        );
    }

    public static Map<Long, Organization> getOrganizationsMap() {
        return organizationsMap;
    }
}
