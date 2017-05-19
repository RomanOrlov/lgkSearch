package lgk.nsbc.backend.entity.viinerve;

import lgk.nsbc.backend.entity.NbcCrNrvLesions;
import lgk.nsbc.backend.entity.NbcStud;
import lgk.nsbc.backend.entity.dictionary.NbcDicDynamic;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
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

}