package lgk.nsbc.backend.entity.viiinerve;

import lgk.nsbc.backend.entity.NbcCrNrvLesions;
import lgk.nsbc.backend.entity.NbcStud;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "NBC_VIIIN_1")
public class NbcViiin implements Serializable {
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

    @OneToOne
    @JoinColumn(name = "HEARING_DISFUNC")
    private NbcViiinHearingDisfunc nbcViiinHearingDisfunc;

    @OneToOne
    @JoinColumn(name = "GARD_ROBERT")
    private NbcViiinGardRobert nbcViiinGardRobert;

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

    public NbcViiinHearingDisfunc getNbcViiinHearingDisfunc() {
        return nbcViiinHearingDisfunc;
    }

    public void setNbcViiinHearingDisfunc(NbcViiinHearingDisfunc nbcViiinHearingDisfunc) {
        this.nbcViiinHearingDisfunc = nbcViiinHearingDisfunc;
    }

    public NbcViiinGardRobert getNbcViiinGardRobert() {
        return nbcViiinGardRobert;
    }

    public void setNbcViiinGardRobert(NbcViiinGardRobert nbcViiinGardRobert) {
        this.nbcViiinGardRobert = nbcViiinGardRobert;
    }
}