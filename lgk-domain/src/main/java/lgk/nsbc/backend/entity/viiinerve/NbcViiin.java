package lgk.nsbc.backend.entity.viiinerve;

import lgk.nsbc.backend.entity.NbcCrNrvLesions;
import lgk.nsbc.backend.entity.NbcStud;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
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

}