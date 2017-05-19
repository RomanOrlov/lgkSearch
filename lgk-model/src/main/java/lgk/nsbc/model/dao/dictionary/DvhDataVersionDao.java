package lgk.nsbc.model.dao.dictionary;

import lgk.nsbc.model.dictionary.DvhDataVersion;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static lgk.nsbc.generated.tables.NbcDvhDataVersion.NBC_DVH_DATA_VERSION;

@Service
public class DvhDataVersionDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    private static Map<Long, DvhDataVersion> dvhDataVersionMap;

    @PostConstruct
    void init() {
        dvhDataVersionMap = Collections.unmodifiableMap(context.fetch(NBC_DVH_DATA_VERSION)
                .stream()
                .map(DvhDataVersion::buildFromRecord)
                .collect(toMap(DvhDataVersion::getN, identity()))
        );
    }

    public static Map<Long, DvhDataVersion> getDvhDataVersionMap() {
        return dvhDataVersionMap;
    }
}
