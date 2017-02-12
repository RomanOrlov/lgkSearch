package lgk.nsbc.backend.entity;

import lgk.nsbc.backend.entity.dictionary.NbcDicDynamic;
import lgk.nsbc.backend.entity.dictionary.NbcSymptomatologyGrade;
import lgk.nsbc.backend.entity.dictionary.NbcSymptomatologyName;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "NBC_SYMPTOMATOLOGY_1")
public class NbcSymptomatology implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne
    @JoinColumn(name = "NAME")
    private NbcSymptomatologyName nbcSymptomatologyName;

    @OneToOne
    @JoinColumn(name = "GRADE")
    private NbcSymptomatologyGrade nbcSymptomatologyGrade;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_STUD_N")
    private List<NbcStud> nbcStudies;

    @OneToOne
    @JoinColumn(name = "DYNAMIC")
    private NbcDicDynamic nbcDicDynamic;

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public NbcSymptomatologyName getNbcSymptomatologyName() {
        return nbcSymptomatologyName;
    }

    public void setNbcSymptomatologyName(NbcSymptomatologyName nbcSymptomatologyName) {
        this.nbcSymptomatologyName = nbcSymptomatologyName;
    }

    public NbcSymptomatologyGrade getNbcSymptomatologyGrade() {
        return nbcSymptomatologyGrade;
    }

    public void setNbcSymptomatologyGrade(NbcSymptomatologyGrade nbcSymptomatologyGrade) {
        this.nbcSymptomatologyGrade = nbcSymptomatologyGrade;
    }

    public NbcDicDynamic getNbcDicDynamic() {
        return nbcDicDynamic;
    }

    public void setNbcDicDynamic(NbcDicDynamic nbcDicDynamic) {
        this.nbcDicDynamic = nbcDicDynamic;
    }

    public List<NbcStud> getNbcStudies() {
        return nbcStudies;
    }

    public void setNbcStudies(List<NbcStud> nbcStudies) {
        this.nbcStudies = nbcStudies;
    }
}