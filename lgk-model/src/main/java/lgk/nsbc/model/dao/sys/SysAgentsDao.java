package lgk.nsbc.model.dao.sys;

import lgk.nsbc.model.sys.SysAgents;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.SysAgents.SYS_AGENTS;

@Service
public class SysAgentsDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;

    public SysAgents findByPid(String pid) {
        return SysAgents.buildFromRecord(context.fetchOne(SYS_AGENTS, SYS_AGENTS.PID.eq(pid)));
    }
}
