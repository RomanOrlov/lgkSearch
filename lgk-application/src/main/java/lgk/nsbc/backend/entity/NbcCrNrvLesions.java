package lgk.nsbc.backend.entity;

import lgk.nsbc.backend.entity.dictionary.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "NBC_CR_NRV_LESIONS_1")
public class NbcCrNrvLesions implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_STUD_N")
    private NbcStud nbcStud;

    @OneToOne
    @JoinColumn(name = "NBC_DIC_LOCSD_N")
    private NbcDicLocsd nbcDicLocsd;

    @OneToOne
    @JoinColumn(name = "NBC_DIC_BODY_PART_N")
    private NbcDicBodyPart nbcDicBodyPart;

    @OneToOne
    @JoinColumn(name = "GRADE")
    private NbcSymptomatologyGrade nbcSymptomatologyGrade;

    @OneToOne
    @JoinColumn(name = "DYNAMIC")
    private NbcDicDynamic nbcDicDynamic;

    @OneToOne
    @JoinColumn(name = "NBC_DIC_CRANIAL_NERVES_N")
    private NbcDicCranialNerves nbcDicCranialNerves;

    @Column(name = "LESSION_PRESENCE")
    private Character lessionPresence;

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

    public NbcDicLocsd getNbcDicLocsd() {
        return nbcDicLocsd;
    }

    public void setNbcDicLocsd(NbcDicLocsd nbcDicLocsd) {
        this.nbcDicLocsd = nbcDicLocsd;
    }

    public NbcDicBodyPart getNbcDicBodyPart() {
        return nbcDicBodyPart;
    }

    public void setNbcDicBodyPart(NbcDicBodyPart nbcDicBodyPart) {
        this.nbcDicBodyPart = nbcDicBodyPart;
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

    public NbcDicCranialNerves getNbcDicCranialNerves() {
        return nbcDicCranialNerves;
    }

    public void setNbcDicCranialNerves(NbcDicCranialNerves nbcDicCranialNerves) {
        this.nbcDicCranialNerves = nbcDicCranialNerves;
    }

    public Character getLessionPresence() {
        return lessionPresence;
    }

    public void setLessionPresence(Character lessionPresence) {
        this.lessionPresence = lessionPresence;
    }
}