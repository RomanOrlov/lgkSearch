package lgk.nsbc.backend.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.*;

/**
 * Параметры облучения мишени
 */
@Entity
@Table(name = "NBC_TRGT_TREAT")
public class NbcTrgtTreat implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne
    @JoinColumn(name = "NBC_TARGET_N")
    private NbcTarget nbcTarget;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_DVH_N")
    private List<NbcDvh> nbcDvh;

    @ManyToOne
    @JoinColumn(name = "NBC_PROC_N")
    private NbcProc nbcProc;

    @Column(name = "PD")
    private Double pd; // Предписанная доза (кравевая, за фракцию)

    @Column(name = "PI")
    private Double pi; // Предписанная изодоза %

    @Column(name = "DPF")
    private Double dpf; // Средняя разовая очаговая доза

    @Column(name = "N_FRACTIONS")
    private Integer nFractions; // Число фракций

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Double getPd() {
        return pd;
    }

    public void setPd(Double pd) {
        this.pd = pd;
    }

    public Double getPi() {
        return pi;
    }

    public void setPi(Double pi) {
        this.pi = pi;
    }

    public Double getDpf() {
        return dpf;
    }

    public void setDpf(Double dpf) {
        this.dpf = dpf;
    }

    public Integer getNFractions() {
        return nFractions;
    }

    public void setNFractions(Integer nFractions) {
        this.nFractions = nFractions;
    }

    public List<NbcDvh> getNbcDvh() {
        return nbcDvh;
    }

    public void setNbcDvh(List<NbcDvh> nbcDvh) {
        this.nbcDvh = nbcDvh;
    }
}