package lgk.nsbc.backend.entity;

import lgk.nsbc.backend.entity.dictionary.NbcExecutorRoles;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "NBC_EXECUTOR")
public class NbcExecutor implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne
    @JoinColumn(name = "NBC_STAFF_N")
    private NbcStaff nbcStaff;

    @OneToOne
    @JoinColumn(name = "ROLES")
    private NbcExecutorRoles nbcExecutorRoles;

    @Column(name = "CONFIRMATION")
    private Character confirmation;

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Character getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(Character confirmation) {
        this.confirmation = confirmation;
    }

    public NbcStaff getNbcStaff() {
        return nbcStaff;
    }

    public void setNbcStaff(NbcStaff nbcStaff) {
        this.nbcStaff = nbcStaff;
    }

    public NbcExecutorRoles getNbcExecutorRoles() {
        return nbcExecutorRoles;
    }

    public void setNbcExecutorRoles(NbcExecutorRoles nbcExecutorRoles) {
        this.nbcExecutorRoles = nbcExecutorRoles;
    }
}
