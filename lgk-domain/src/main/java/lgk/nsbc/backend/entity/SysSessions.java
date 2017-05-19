package lgk.nsbc.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
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