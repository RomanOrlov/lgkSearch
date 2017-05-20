package lgk.nsbc.model.dao;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class SampleDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;
}
