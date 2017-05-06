package lgk.nsbc.model.dao.histology;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MutationsDao {
    @Autowired
    private DSLContext context;
}
