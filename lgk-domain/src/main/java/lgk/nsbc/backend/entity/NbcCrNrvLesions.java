package lgk.nsbc.backend.entity;

import lgk.nsbc.backend.entity.dictionary.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Поражение черепно мозковых нервов
 */
@Setter
@Getter
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

}