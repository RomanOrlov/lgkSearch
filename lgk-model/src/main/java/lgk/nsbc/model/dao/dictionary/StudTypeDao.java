package lgk.nsbc.model.dao.dictionary;

import lgk.nsbc.model.dictionary.StudType;
import lombok.Getter;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static lgk.nsbc.generated.tables.NbcStudStudyType.NBC_STUD_STUDY_TYPE;

@Service
public class StudTypeDao {
    @Autowired
    private DSLContext context;

    @Getter
    private Map<Long, StudType> studTypeMap;

    @PostConstruct
    void init() {
        studTypeMap = Collections.unmodifiableMap(context.fetch(NBC_STUD_STUDY_TYPE)
                .stream()
                .map(StudType::buildFromRecord)
                .collect(toMap(StudType::getN, identity()))
        );
    }
}
