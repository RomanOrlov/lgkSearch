package lgk.nsbc.backend.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "NBC_SCTPERF")
public class NbcSctperf implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_STUD_N")
    private NbcStud nbcStud;

    @Column(name = "CBF")
    private Double cbf;

    @Column(name = "CBV")
    private Double cbv;

    @Column(name = "MTT")
    private Double mtt;

    @Column(name = "PS")
    private Double ps;

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Double getCbf() {
        return cbf;
    }

    public void setCbf(Double cbf) {
        this.cbf = cbf;
    }

    public Double getCbv() {
        return cbv;
    }

    public void setCbv(Double cbv) {
        this.cbv = cbv;
    }

    public Double getMtt() {
        return mtt;
    }

    public void setMtt(Double mtt) {
        this.mtt = mtt;
    }

    public Double getPs() {
        return ps;
    }

    public void setPs(Double ps) {
        this.ps = ps;
    }

    public NbcStud getNbcStud() {
        return nbcStud;
    }

    public void setNbcStud(NbcStud nbcStud) {
        this.nbcStud = nbcStud;
    }
}