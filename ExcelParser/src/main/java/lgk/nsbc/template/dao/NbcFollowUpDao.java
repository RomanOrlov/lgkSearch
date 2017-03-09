package lgk.nsbc.template.dao;

import lgk.nsbc.template.model.NbcFollowUp;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NbcFollowUpDao {
    @Autowired
    private DSLContext context;

    public void saveFollowUp(NbcFollowUp nbcFollowUp) {

    }
}
