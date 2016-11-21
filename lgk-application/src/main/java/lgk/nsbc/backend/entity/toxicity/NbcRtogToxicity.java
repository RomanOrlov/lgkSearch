package lgk.nsbc.backend.entity.toxicity;

import lgk.nsbc.backend.entity.NbcPatients;
import lgk.nsbc.backend.entity.NbcStud;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "NBC_RTOG_TOXICITY")
public class NbcRtogToxicity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_STUD_N")
    private NbcStud nbcStud;

    @Column(name = "AMOUNT")
    private Integer amount;

    @OneToOne
    @JoinColumn(name = "GRADE")
    private NbcRtogToxicityGrade nbcRtogToxicityGrade;

    @OneToOne
    @JoinColumn(name = "PERIOD")
    private NbcRtogToxicityPeriod nbcRtogToxicityPeriod;

    @Column(name = "COMMENTARY")
    private String commentary;

    @Column(name = "BEGIN_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date beginDate;

    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @OneToOne
    @JoinColumn(name = "NBC_PATIENTS_N")
    private NbcPatients nbcPatients;

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public NbcStud getNbcStud() {
        return nbcStud;
    }

    public void setNbcStud(NbcStud nbcStud) {
        this.nbcStud = nbcStud;
    }

    public NbcRtogToxicityGrade getNbcRtogToxicityGrade() {
        return nbcRtogToxicityGrade;
    }

    public void setNbcRtogToxicityGrade(NbcRtogToxicityGrade nbcRtogToxicityGrade) {
        this.nbcRtogToxicityGrade = nbcRtogToxicityGrade;
    }

    public NbcRtogToxicityPeriod getNbcRtogToxicityPeriod() {
        return nbcRtogToxicityPeriod;
    }

    public void setNbcRtogToxicityPeriod(NbcRtogToxicityPeriod nbcRtogToxicityPeriod) {
        this.nbcRtogToxicityPeriod = nbcRtogToxicityPeriod;
    }

    public NbcPatients getNbcPatients() {
        return nbcPatients;
    }

    public void setNbcPatients(NbcPatients nbcPatients) {
        this.nbcPatients = nbcPatients;
    }
}
