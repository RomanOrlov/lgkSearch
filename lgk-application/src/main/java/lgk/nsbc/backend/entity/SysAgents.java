package lgk.nsbc.backend.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SYS_AGENTS")
public class SysAgents implements Serializable {
    private static final long serialVersionUID = 1L;

    @OneToOne
    @JoinColumn(name = "PEOPLE_N")
    @PrimaryKeyJoinColumn
    private BasPeople basPeople;

    @OneToOne
    @JoinColumn(name = "NBC_ORG_N_DEAFULT")
    @PrimaryKeyJoinColumn
    private NbcOrganizations nbcOrganizations;

    @Id
    @Column(name = "N")
    private Integer n;

    @Column(name = "OP_CREATE")
    private Integer opCreate;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PID")
    private String pid;

    public SysAgents() {
    }

    public SysAgents(Integer n) {
        this.n = n;
    }

    public SysAgents(Integer n, Integer opCreate, String name, String pid) {
        this.n = n;
        this.opCreate = opCreate;
        this.name = name;
        this.pid = pid;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public long getOpCreate() {
        return opCreate;
    }

    public void setOpCreate(Integer opCreate) {
        this.opCreate = opCreate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public BasPeople getBasPeople() {
        return basPeople;
    }

    public void setBasPeople(BasPeople basPeople) {
        this.basPeople = basPeople;
    }

    public NbcOrganizations getNbcOrganizations() {
        return nbcOrganizations;
    }

    public void setNbcOrganizations(NbcOrganizations nbcOrganizations) {
        this.nbcOrganizations = nbcOrganizations;
    }
}