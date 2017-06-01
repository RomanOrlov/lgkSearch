package lgk.nsbc.model.dao.dictionary;

import lgk.nsbc.model.dictionary.StudType;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static lgk.nsbc.generated.tables.StudStudyType.STUD_STUDY_TYPE;

@Service
public class StudTypeDao implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final long MRI_TYPE = 1L;

    @Autowired
    private DSLContext context;

    private static Map<Long, StudType> studTypeMap;

    @PostConstruct
    void init() {
        studTypeMap = Collections.unmodifiableMap(context.fetch(STUD_STUDY_TYPE)
                .stream()
                .map(StudType::buildFromRecord)
                .collect(toMap(StudType::getN, identity()))
        );
    }

    public static Map<Long, StudType> getStudTypeMap() {
        return studTypeMap;
    }
}
