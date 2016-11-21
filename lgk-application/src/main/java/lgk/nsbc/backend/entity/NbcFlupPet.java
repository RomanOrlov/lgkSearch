package lgk.nsbc.backend.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * ПЭТ характеристика мишени
 */
@Entity
@Table(name = "NBC_FLUP_PET")
public class NbcFlupPet implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne
    @JoinColumn(name = "NBC_FOLLOWUP_N")
    private NbcFollowup nbcFollowup;

    @Column(name = "IC")
    private Double ic;

    @Column(name = "SUV_MAX")
    private Double suvMax;

    @Column(name = "SUV_MEAN")
    private Double suvMean;

    @Column(name = "SUVR_MAX")
    private Double suvrMax;

    @Column(name = "SUVR_MEAN")
    private Double suvrMean;

    @Column(name = "MTV")
    private Double mtv;

    @Column(name = "TIME_")
    private Double time;

    @Column(name = "PHASE")
    private Integer phase;

    @Column(name = "COMMENTARY")
    private String commentary;

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Double getIc() {
        return ic;
    }

    public void setIc(Double ic) {
        this.ic = ic;
    }

    public Double getSuvMax() {
        return suvMax;
    }

    public void setSuvMax(Double suvMax) {
        this.suvMax = suvMax;
    }

    public Double getSuvMean() {
        return suvMean;
    }

    public void setSuvMean(Double suvMean) {
        this.suvMean = suvMean;
    }

    public Double getSuvrMax() {
        return suvrMax;
    }

    public void setSuvrMax(Double suvrMax) {
        this.suvrMax = suvrMax;
    }

    public Double getSuvrMean() {
        return suvrMean;
    }

    public void setSuvrMean(Double suvrMean) {
        this.suvrMean = suvrMean;
    }

    public Double getMtv() {
        return mtv;
    }

    public void setMtv(Double mtv) {
        this.mtv = mtv;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public Integer getPhase() {
        return phase;
    }

    public void setPhase(Integer phase) {
        this.phase = phase;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public NbcFollowup getNbcFollowup() {
        return nbcFollowup;
    }

    public void setNbcFollowup(NbcFollowup nbcFollowup) {
        this.nbcFollowup = nbcFollowup;
    }
}