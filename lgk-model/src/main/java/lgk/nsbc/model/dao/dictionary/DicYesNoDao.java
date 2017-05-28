package lgk.nsbc.model.dao.dictionary;

import lgk.nsbc.model.dictionary.DicYesNo;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static lgk.nsbc.generated.tables.DicYesNo.DIC_YES_NO;

@Service
public class DicYesNoDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    private static Map<Long, DicYesNo> dicYesNo;

    @PostConstruct
    void init() {
        dicYesNo = Collections.unmodifiableMap(context.fetch(DIC_YES_NO)
                .stream()
                .map(DicYesNo::buildFromRecord)
                .collect(toMap(DicYesNo::getN, identity()))
        );
    }

    public static Map<Long, DicYesNo> getDicYesNo() {
        return dicYesNo;
    }
}
