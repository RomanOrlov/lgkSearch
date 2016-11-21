package lgk.nsbc.backend.entity.rfp;

import lgk.nsbc.backend.entity.NbcStud;
import lgk.nsbc.backend.entity.rfp.NbcRfp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Параметры вводимых препаратов (контраст для КТ, МРТ) РФП для ПЭТ (или ОФЕКТ)
 */
@Entity
@Table(name = "NBC_STUD_INJ")
public class NbcStudInj implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_STUD_N")
    private NbcStud nbcStud;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_RFP_N")
    private NbcRfp nbcRfp;

    @Column(name = "INJ_ACTIVITY_BQ")
    private Double injActivityBq;

    @Column(name = "INJ_VOL_ML")
    private Double injVolMl;

    @Column(name = "INJ_BEGIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date injBegin;

    @Column(name = "INJ_END")
    @Temporal(TemporalType.TIMESTAMP)
    private Date injEnd;

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Double getInjActivityBq() {
        return injActivityBq;
    }

    public void setInjActivityBq(Double injActivityBq) {
        this.injActivityBq = injActivityBq;
    }

    public Double getInjVolMl() {
        return injVolMl;
    }

    public void setInjVolMl(Double injVolMl) {
        this.injVolMl = injVolMl;
    }

    public Date getInjBegin() {
        return injBegin;
    }

    public void setInjBegin(Date injBegin) {
        this.injBegin = injBegin;
    }

    public Date getInjEnd() {
        return injEnd;
    }

    public void setInjEnd(Date injEnd) {
        this.injEnd = injEnd;
    }

    public NbcStud getNbcStud() {
        return nbcStud;
    }

    public void setNbcStud(NbcStud nbcStud) {
        this.nbcStud = nbcStud;
    }

    public NbcRfp getNbcRfp() {
        return nbcRfp;
    }

    public void setNbcRfp(NbcRfp nbcRfp) {
        this.nbcRfp = nbcRfp;
    }
}