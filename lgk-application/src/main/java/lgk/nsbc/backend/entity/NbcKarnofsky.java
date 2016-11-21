package lgk.nsbc.backend.entity;

import lgk.nsbc.backend.entity.dictionary.NbcKarnofskyGrade;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "NBC_KARNOFSKY_1")
public class NbcKarnofsky implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_STUD_N")
    private NbcStud nbcStud;

    @OneToOne
    @JoinColumn(name = "GRADE")
    private NbcKarnofskyGrade nbcKarnofskyGrade;

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

    public NbcKarnofskyGrade getNbcKarnofskyGrade() {
        return nbcKarnofskyGrade;
    }

    public void setNbcKarnofskyGrade(NbcKarnofskyGrade nbcKarnofskyGrade) {
        this.nbcKarnofskyGrade = nbcKarnofskyGrade;
    }
}