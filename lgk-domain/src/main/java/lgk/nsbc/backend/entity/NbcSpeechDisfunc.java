package lgk.nsbc.backend.entity;

import lgk.nsbc.backend.entity.dictionary.NbcDicDynamic;
import lgk.nsbc.backend.entity.dictionary.NbcSpeechDisfuncGrade;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "NBC_SPEECH_DISFUNC_1")
public class NbcSpeechDisfunc implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_STUD_N")
    private NbcStud nbcStud;

    @OneToOne
    @JoinColumn(name = "GRADE")
    private NbcSpeechDisfuncGrade nbcSymptomatologyGrade;

    @OneToOne
    @JoinColumn(name = "DYNAMIC")
    private NbcDicDynamic nbcDicDynamic;

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

    public NbcDicDynamic getNbcDicDynamic() {
        return nbcDicDynamic;
    }

    public void setNbcDicDynamic(NbcDicDynamic nbcDicDynamic) {
        this.nbcDicDynamic = nbcDicDynamic;
    }

    public NbcSpeechDisfuncGrade getNbcSymptomatologyGrade() {
        return nbcSymptomatologyGrade;
    }

    public void setNbcSymptomatologyGrade(NbcSpeechDisfuncGrade nbcSymptomatologyGrade) {
        this.nbcSymptomatologyGrade = nbcSymptomatologyGrade;
    }
}

