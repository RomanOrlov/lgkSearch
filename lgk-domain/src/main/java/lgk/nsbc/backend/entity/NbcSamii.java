package lgk.nsbc.backend.entity;

import lgk.nsbc.backend.entity.dictionary.NbcSamiiGrade;
import lgk.nsbc.backend.entity.target.NbcTarget;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "NBC_SAMII_1")
public class NbcSamii implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_STUD_N")
    private NbcStud nbcStud;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_TARGET_N")
    private NbcTarget nbcTarget;

    @OneToOne
    @JoinColumn(name = "GRADE")
    private NbcSamiiGrade nbcSamiiGrade;

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

    public NbcTarget getNbcTarget() {
        return nbcTarget;
    }

    public void setNbcTarget(NbcTarget nbcTarget) {
        this.nbcTarget = nbcTarget;
    }

    public NbcSamiiGrade getNbcSamiiGrade() {
        return nbcSamiiGrade;
    }

    public void setNbcSamiiGrade(NbcSamiiGrade nbcSamiiGrade) {
        this.nbcSamiiGrade = nbcSamiiGrade;
    }
}