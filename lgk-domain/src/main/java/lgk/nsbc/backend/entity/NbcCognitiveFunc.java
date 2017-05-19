package lgk.nsbc.backend.entity;

import lgk.nsbc.backend.entity.dictionary.NbcCognitiveFuncGds;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "NBC_COGNITIVE_FUNC")
public class NbcCognitiveFunc implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_STUD_N")
    private NbcStud nbcStud;

    @Column(name = "MMSE")
    private Integer mmse; // Оценка когн. функ. по шкале MSE (0-30)

    @Column(name = "MOCA")
    private Integer moca; // Оценка когн. функ. по шкале MoCa (0-30)

    @OneToOne
    @JoinColumn(name = "GDS")
    private NbcCognitiveFuncGds nbcCognitiveFuncGds;

}