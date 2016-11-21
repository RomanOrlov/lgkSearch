package lgk.nsbc.backend.entity.vnerve;

import lgk.nsbc.backend.entity.NbcCrNrvLesions;
import lgk.nsbc.backend.entity.NbcStud;
import lgk.nsbc.backend.entity.vnerve.NbcVnHypostesy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "NBC_VN_1")
public class NbcVn implements Serializable {
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

    @Column(name = "NEUROPATHY")
    private Character neuropathy;

    @Column(name = "NEURALGIA")
    private Character neuralgia;

    @Column(name = "PAIN")
    private Character pain;

    @OneToOne
    @JoinColumn(name = "HYPOSTESY")
    private NbcVnHypostesy nbcVnHypostesy;

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

    public NbcCrNrvLesions getNbcCrNrvLesions() {
        return nbcCrNrvLesions;
    }

    public void setNbcCrNrvLesions(NbcCrNrvLesions nbcCrNrvLesions) {
        this.nbcCrNrvLesions = nbcCrNrvLesions;
    }

    public Character getNeuropathy() {
        return neuropathy;
    }

    public void setNeuropathy(Character neuropathy) {
        this.neuropathy = neuropathy;
    }

    public Character getNeuralgia() {
        return neuralgia;
    }

    public void setNeuralgia(Character neuralgia) {
        this.neuralgia = neuralgia;
    }

    public Character getPain() {
        return pain;
    }

    public void setPain(Character pain) {
        this.pain = pain;
    }

    public NbcVnHypostesy getNbcVnHypostesy() {
        return nbcVnHypostesy;
    }

    public void setNbcVnHypostesy(NbcVnHypostesy nbcVnHypostesy) {
        this.nbcVnHypostesy = nbcVnHypostesy;
    }
}