package lgk.nsbc.backend.entity.vnerve;

import lgk.nsbc.backend.entity.NbcCrNrvLesions;
import lgk.nsbc.backend.entity.NbcStud;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
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

}