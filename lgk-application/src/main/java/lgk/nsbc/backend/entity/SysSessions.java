package lgk.nsbc.backend.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "SYS_SESSIONS")
public class SysSessions implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @Column(name = "SID")
    private String sid;

    @Column(name = "AGENT_N")
    private Integer agentN;

    @Column(name = "OPENED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date opened;

    public SysSessions() {
    }

    public SysSessions(Integer n) {
        this.n = n;
    }

    public SysSessions(Integer n, String sid, Integer agentN, Date opened) {
        this.n = n;
        this.sid = sid;
        this.agentN = agentN;
        this.opened = opened;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public long getAgentN() {
        return agentN;
    }

    public void setAgentN(Integer agentN) {
        this.agentN = agentN;
    }

    public Date getOpened() {
        return opened;
    }

    public void setOpened(Date opened) {
        this.opened = opened;
    }
}
