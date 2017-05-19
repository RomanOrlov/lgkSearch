package lgk.nsbc.backend.entity.vasculardisarders;

import lgk.nsbc.backend.entity.NbcStud;
import lgk.nsbc.backend.entity.target.NbcTarget;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "NBC_VASCULAR_DISORDERS_1")
public class NbcVascularDisorders implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_STUD_N")
    private NbcStud nbcStud;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_TARGET_N")
    private NbcTarget nbcTarget;

    @OneToOne
    @JoinColumn(name = "NBC_AFFERENTS_1_N")
    private NbcAfferents nbcAfferents;

    @OneToOne
    @JoinColumn(name = "NBC_EFFERENTS_1_N")
    private NbcEfferents nbcEfferents;

    @Column(name = "ELOQUENCE_1")
    private Character eloquence1;

    @Column(name = "SPETZLER_MARTIN_GRADE")
    private Integer spetzlerMartinGrade;

}