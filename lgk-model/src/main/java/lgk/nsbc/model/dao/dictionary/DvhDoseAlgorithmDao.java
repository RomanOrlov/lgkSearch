package lgk.nsbc.model.dao.dictionary;

import lgk.nsbc.model.dictionary.DvhDoseAlgorithm;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static lgk.nsbc.generated.tables.NbcDvhDoseAlgorithm.NBC_DVH_DOSE_ALGORITHM;

@Service
public class DvhDoseAlgorithmDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    private static Map<Long, DvhDoseAlgorithm> dvhDoseAlgorithmMap;

    @PostConstruct
    void init() {
        dvhDoseAlgorithmMap = Collections.unmodifiableMap(context.fetch(NBC_DVH_DOSE_ALGORITHM)
                .stream()
                .map(DvhDoseAlgorithm::buildFromRecord)
                .collect(toMap(DvhDoseAlgorithm::getN, identity()))
        );
    }

    public static Map<Long, DvhDoseAlgorithm> getDvhDoseAlgorithmMap() {
        return dvhDoseAlgorithmMap;
    }
}
