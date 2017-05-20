package lgk.nsbc.model.dao.dictionary;

import lgk.nsbc.model.dictionary.DvhLgpVersion;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static lgk.nsbc.generated.tables.NbcDvhLgpVersion.NBC_DVH_LGP_VERSION;

@Service
public class DvhLgpVersionDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    private static Map<Long, DvhLgpVersion> dvhLgpVersionMap;

    @PostConstruct
    void init() {
        dvhLgpVersionMap = Collections.unmodifiableMap(context.fetch(NBC_DVH_LGP_VERSION)
                .stream()
                .map(DvhLgpVersion::buildFromRecord)
                .collect(toMap(DvhLgpVersion::getN, identity()))
        );
    }

    public static Map<Long, DvhLgpVersion> getDvhLgpVersionMap() {
        return dvhLgpVersionMap;
    }
}
