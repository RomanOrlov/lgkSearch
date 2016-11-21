package lgk.nsbc.backend.entity.viinerve;

import lgk.nsbc.backend.entity.NbcCrNrvLesions;
import lgk.nsbc.backend.entity.NbcStud;
import lgk.nsbc.backend.entity.dictionary.NbcDicDynamic;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "NBC_VIIN_1")
public class NbcViin implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_STUD_N")
    private NbcStud nbcStud;

    @OneToOne
    @JoinColumn(name = "NBC_CR_NRV_LESIONS_1_N")
    private NbcCrNrvLesions nbcCrNrvLesions;

    @Column(name = "CENTRAL_PARESIS")
    private Character centralParesis;

    @Column(name = "PEREFERICAL_PARESIS")
    private Character perefericalParesis;

    @OneToOne
    @JoinColumn(name = "HOUSE_BRACKM")
    private NbcViinHouseBrackm nbcViinHouseBrackm;

    @Column(name = "TIC_PRESENCE")
    private Character ticPresence;

    @OneToOne
    @JoinColumn(name = "TIC")
    private NbcViinTic nbcViinTic;

    @OneToOne
    @JoinColumn(name = "TIC_DYNAMIC")
    private NbcDicDynamic ticDynamic;

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Character getCentralParesis() {
        return centralParesis;
    }

    public void setCentralParesis(Character centralParesis) {
        this.centralParesis = centralParesis;
    }

    public Character getPerefericalParesis() {
        return perefericalParesis;
    }

    public void setPerefericalParesis(Character perefericalParesis) {
        this.perefericalParesis = perefericalParesis;
    }

    public Character getTicPresence() {
        return ticPresence;
    }

    public void setTicPresence(Character ticPresence) {
        this.ticPresence = ticPresence;
    }

    public NbcViinHouseBrackm getNbcViinHouseBrackm() {
        return nbcViinHouseBrackm;
    }

    public void setNbcViinHouseBrackm(NbcViinHouseBrackm nbcViinHouseBrackm) {
        this.nbcViinHouseBrackm = nbcViinHouseBrackm;
    }

    public NbcCrNrvLesions getNbcCrNrvLesions() {
        return nbcCrNrvLesions;
    }

    public void setNbcCrNrvLesions(NbcCrNrvLesions nbcCrNrvLesions) {
        this.nbcCrNrvLesions = nbcCrNrvLesions;
    }

    public NbcStud getNbcStud() {
        return nbcStud;
    }

    public void setNbcStud(NbcStud nbcStud) {
        this.nbcStud = nbcStud;
    }

    public NbcViinTic getNbcViinTic() {
        return nbcViinTic;
    }

    public void setNbcViinTic(NbcViinTic nbcViinTic) {
        this.nbcViinTic = nbcViinTic;
    }

    public NbcDicDynamic getTicDynamic() {
        return ticDynamic;
    }

    public void setTicDynamic(NbcDicDynamic ticDynamic) {
        this.ticDynamic = ticDynamic;
    }
}