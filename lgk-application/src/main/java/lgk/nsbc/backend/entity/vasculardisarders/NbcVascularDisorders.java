package lgk.nsbc.backend.entity.vasculardisarders;

import lgk.nsbc.backend.entity.NbcStud;
import lgk.nsbc.backend.entity.target.NbcTarget;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "NBC_VASCULAR_DISORDERS_1")
public class NbcVascularDisorders implements Serializable {
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
    @JoinColumn(name = "NBC_AFFERENTS_1_N")
    private NbcAfferents nbcAfferents;

    @OneToOne
    @JoinColumn(name = "NBC_EFFERENTS_1_N")
    private NbcEfferents nbcEfferents;

    @Column(name = "ELOQUENCE_1")
    private Character eloquence1;

    @Column(name = "SPETZLER_MARTIN_GRADE")
    private Integer spetzlerMartinGrade;

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

    public NbcAfferents getNbcAfferents() {
        return nbcAfferents;
    }

    public void setNbcAfferents(NbcAfferents nbcAfferents) {
        this.nbcAfferents = nbcAfferents;
    }

    public NbcEfferents getNbcEfferents() {
        return nbcEfferents;
    }

    public void setNbcEfferents(NbcEfferents nbcEfferents) {
        this.nbcEfferents = nbcEfferents;
    }

    public Character getEloquence1() {
        return eloquence1;
    }

    public void setEloquence1(Character eloquence1) {
        this.eloquence1 = eloquence1;
    }

    public Integer getSpetzlerMartinGrade() {
        return spetzlerMartinGrade;
    }

    public void setSpetzlerMartinGrade(Integer spetzlerMartinGrade) {
        this.spetzlerMartinGrade = spetzlerMartinGrade;
    }
}