package lgk.nsbc.backend.entity.eye;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "NBC_VISION_1")
public class NbcVision implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne
    @JoinColumn(name = "CORRECTION")
    private NbcVisionCorrection nbcVisionCorrection;

    @Column(name = "AMOUNT")
    private Double amount;

    @OneToMany
    @JoinColumn(name = "NBC_EYE_1_N")
    private List<NbcEye> nbcEyesList;

    @OneToOne
    @JoinColumn(name = "GRADE")
    private NbcVisionGrade nbcVisionGrade;

}