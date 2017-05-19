package lgk.nsbc.backend.entity;

import lgk.nsbc.backend.entity.dictionary.NbcOrganizations;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "SYS_AGENTS")
@NamedEntityGraphs({
        @NamedEntityGraph(name = "allSysAgents",
                attributeNodes = {
                        @NamedAttributeNode("basPeople"),
                        @NamedAttributeNode("nbcOrganizations")
                })
})
public class SysAgents implements Serializable {
    private static final long serialVersionUID = 1L;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PEOPLE_N")
    private BasPeople basPeople;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_ORG_N_DEAFULT")
    private NbcOrganizations nbcOrganizations;

    @Id
    @Column(name = "N")
    private Integer n;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PID")
    private String pid;

    @Override
    public String toString() {
        return "SysAgents{" +
                "basPeople=" + basPeople +
                ", n=" + n +
                ", name='" + name + '\'' +
                ", pid='" + pid + '\'' +
                '}';
    }
}