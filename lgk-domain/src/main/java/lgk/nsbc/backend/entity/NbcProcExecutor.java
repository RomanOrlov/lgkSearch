package lgk.nsbc.backend.entity;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "NBC_PROC_EXECUTOR")
public class NbcProcExecutor implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne
    @JoinColumn(name = "NBC_PROC_N")
    private NbcProc nbcProc;

    @OneToOne
    @JoinColumn(name = "NBC_EXECUTOR_N")
    private NbcExecutor nbcExecutor;

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public NbcProc getNbcProc() {
        return nbcProc;
    }

    public void setNbcProc(NbcProc nbcProc) {
        this.nbcProc = nbcProc;
    }

    public NbcExecutor getNbcExecutor() {
        return nbcExecutor;
    }

    public void setNbcExecutor(NbcExecutor nbcExecutor) {
        this.nbcExecutor = nbcExecutor;
    }
}