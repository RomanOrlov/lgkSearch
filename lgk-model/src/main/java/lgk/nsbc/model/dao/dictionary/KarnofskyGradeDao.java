package lgk.nsbc.model.dao.dictionary;

import lgk.nsbc.model.dictionary.KarnofskyGrade;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static lgk.nsbc.generated.tables.KarnofskyGrade.KARNOFSKY_GRADE;

@Service
public class KarnofskyGradeDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    private static Map<Long, KarnofskyGrade> karnofskyGradeMap;

    @PostConstruct
    public void init() {
        karnofskyGradeMap = context.fetch(KARNOFSKY_GRADE)
                .stream()
                .map(KarnofskyGrade::buildFromRecord)
                .collect(toMap(KarnofskyGrade::getN, identity()));
    }

    public static Map<Long, KarnofskyGrade> getKarnofskyGradeMap() {
        return karnofskyGradeMap;
    }
}
