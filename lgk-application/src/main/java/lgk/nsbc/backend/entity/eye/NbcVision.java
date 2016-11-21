package lgk.nsbc.backend.entity.eye;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

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

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public NbcVisionCorrection getNbcVisionCorrection() {
        return nbcVisionCorrection;
    }

    public void setNbcVisionCorrection(NbcVisionCorrection nbcVisionCorrection) {
        this.nbcVisionCorrection = nbcVisionCorrection;
    }

    public List<NbcEye> getNbcEyesList() {
        return nbcEyesList;
    }

    public void setNbcEyesList(List<NbcEye> nbcEyesList) {
        this.nbcEyesList = nbcEyesList;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public NbcVisionGrade getNbcVisionGrade() {
        return nbcVisionGrade;
    }

    public void setNbcVisionGrade(NbcVisionGrade nbcVisionGrade) {
        this.nbcVisionGrade = nbcVisionGrade;
    }
}