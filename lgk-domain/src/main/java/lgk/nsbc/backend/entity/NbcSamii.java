package lgk.nsbc.backend.entity;

import lgk.nsbc.backend.entity.dictionary.NbcSamiiGrade;
import lgk.nsbc.backend.entity.target.NbcTarget;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "NBC_SAMII_1")
public class NbcSamii implements Serializable {
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
    @JoinColumn(name = "GRADE")
    private NbcSamiiGrade nbcSamiiGrade;

}