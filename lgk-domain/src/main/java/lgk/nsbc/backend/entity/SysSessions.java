package lgk.nsbc.backend.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "SYS_SESSIONS")
public class SysSessions implements Serializable {
    private static final long serialVersionUID = 1L;

    /*
    @NamedEntityGraph(name = "allSysSessions",
        attributeNodes = {@NamedAttributeNode("sysAgents")})
     */

    @OneToOne
    @JoinColumn(name = "AGENT_N")
    private SysAgents sysAgents;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "N")
    private Integer n;

    @Column(name = "SID")
    private String sid;

    @Column(name = "OPENED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date opened;

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

    public Date getOpened() {
        return opened;
    }

    public void setOpened(Date opened) {
        this.opened = opened;
    }

    public SysAgents getSysAgents() {
        return sysAgents;
    }

    public void setSysAgents(SysAgents sysAgents) {
        this.sysAgents = sysAgents;
    }

    @Override
    public String toString() {
        return "SysSessions{" +
                "sysAgents=" + sysAgents +
                ", n=" + n +
                ", sid='" + sid + '\'' +
                ", opened=" + opened +
                '}';
    }
}