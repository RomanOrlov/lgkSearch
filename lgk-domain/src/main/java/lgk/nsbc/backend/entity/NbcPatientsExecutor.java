package lgk.nsbc.backend.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "NBC_PATIENTS_EXECUTOR")
public class NbcPatientsExecutor implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_PATIENTS_N")
    private NbcPatients nbcPatients;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_EXECUTOR_N")
    private NbcExecutor nbcExecutor;

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public NbcPatients getNbcPatients() {
        return nbcPatients;
    }

    public void setNbcPatients(NbcPatients nbcPatients) {
        this.nbcPatients = nbcPatients;
    }

    public NbcExecutor getNbcExecutor() {
        return nbcExecutor;
    }

    public void setNbcExecutor(NbcExecutor nbcExecutor) {
        this.nbcExecutor = nbcExecutor;
    }
}