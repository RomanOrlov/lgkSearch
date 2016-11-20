package lgk.nsbc.backend.entity;

import lgk.nsbc.backend.entity.dictionary.NbcDicDynamic;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Гидроцефалия
 */
@Entity
@Table(name = "NBC_HYDROCEFALUS_1")
public class NbcHydrocefalus implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_STUD_N")
    private NbcStud nbcStud;

    @OneToOne
    @JoinColumn(name = "DYNAMIC")
    private NbcDicDynamic nbcDicDynamic;

    @Column(name = "LESSION_PRESENCE")
    private Character lessionPresence;

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Character getLessionPresence() {
        return lessionPresence;
    }

    public void setLessionPresence(Character lessionPresence) {
        this.lessionPresence = lessionPresence;
    }

    public NbcDicDynamic getNbcDicDynamic() {
        return nbcDicDynamic;
    }

    public void setNbcDicDynamic(NbcDicDynamic nbcDicDynamic) {
        this.nbcDicDynamic = nbcDicDynamic;
    }

    public NbcStud getNbcStud() {
        return nbcStud;
    }

    public void setNbcStud(NbcStud nbcStud) {
        this.nbcStud = nbcStud;
    }
}