package lgk.nsbc.backend.entity.epilepsy;

import lgk.nsbc.backend.entity.NbcStud;
import lgk.nsbc.backend.entity.epilepsy.NbcEpilepsyEngel;
import lgk.nsbc.backend.entity.epilepsy.NbcEpilepsyFitPeriod;
import lgk.nsbc.backend.entity.epilepsy.NbcEpilepsyFitProperty;
import lgk.nsbc.backend.entity.epilepsy.NbcEpilepsyFitRate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "NBC_EPILEPSY_1")
public class NbcEpilepsy implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_STUD_N")
    private NbcStud nbcStud;

    @Column(name = "AMOUNT_FROM")
    private Integer amountFrom;

    @Column(name = "AMOUNT_TO")
    private Integer amountTo;

    @OneToOne
    @JoinColumn(name = "FIT_PERIOD")
    private NbcEpilepsyFitPeriod nbcEpilepsyFitPeriod;

    @OneToOne
    @JoinColumn(name = "FIT_RATE")
    private NbcEpilepsyFitRate nbcEpilepsyFitRate;

    @OneToOne
    @JoinColumn(name = "FIT_PROPERTY")
    private NbcEpilepsyFitProperty nbcEpilepsyFitProperty;

    @OneToOne
    @JoinColumn(name = "ENGEL")
    private NbcEpilepsyEngel nbcEpilepsyEngel;


    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public NbcStud getNbcStud() {
        return nbcStud;
    }

    public void setNbcStud(NbcStud nbcStud) {
        this.nbcStud = nbcStud;
    }

    public Integer getAmountFrom() {
        return amountFrom;
    }

    public void setAmountFrom(Integer amountFrom) {
        this.amountFrom = amountFrom;
    }

    public Integer getAmountTo() {
        return amountTo;
    }

    public void setAmountTo(Integer amountTo) {
        this.amountTo = amountTo;
    }

    public NbcEpilepsyFitPeriod getNbcEpilepsyFitPeriod() {
        return nbcEpilepsyFitPeriod;
    }

    public void setNbcEpilepsyFitPeriod(NbcEpilepsyFitPeriod nbcEpilepsyFitPeriod) {
        this.nbcEpilepsyFitPeriod = nbcEpilepsyFitPeriod;
    }

    public NbcEpilepsyFitRate getNbcEpilepsyFitRate() {
        return nbcEpilepsyFitRate;
    }

    public void setNbcEpilepsyFitRate(NbcEpilepsyFitRate nbcEpilepsyFitRate) {
        this.nbcEpilepsyFitRate = nbcEpilepsyFitRate;
    }

    public NbcEpilepsyFitProperty getNbcEpilepsyFitProperty() {
        return nbcEpilepsyFitProperty;
    }

    public void setNbcEpilepsyFitProperty(NbcEpilepsyFitProperty nbcEpilepsyFitProperty) {
        this.nbcEpilepsyFitProperty = nbcEpilepsyFitProperty;
    }

    public NbcEpilepsyEngel getNbcEpilepsyEngel() {
        return nbcEpilepsyEngel;
    }

    public void setNbcEpilepsyEngel(NbcEpilepsyEngel nbcEpilepsyEngel) {
        this.nbcEpilepsyEngel = nbcEpilepsyEngel;
    }
}