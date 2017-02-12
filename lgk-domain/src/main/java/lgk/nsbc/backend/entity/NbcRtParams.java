package lgk.nsbc.backend.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "NBC_RT_PARAMS")
public class NbcRtParams implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne
    @JoinColumn(name = "NBC_PROC_N")
    private NbcProc nbcProc;

    @Column(name = "BEAM_ON_TIME")
    private Double beamOnTime;

    @Column(name = "N_SHOTS")
    private Integer nShots;

    @Column(name = "N_BEAMS")
    private Integer nBeams;

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Double getBeamOnTime() {
        return beamOnTime;
    }

    public void setBeamOnTime(Double beamOnTime) {
        this.beamOnTime = beamOnTime;
    }

    public Integer getNShots() {
        return nShots;
    }

    public void setNShots(Integer nShots) {
        this.nShots = nShots;
    }

    public Integer getNBeams() {
        return nBeams;
    }

    public void setNBeams(Integer nBeams) {
        this.nBeams = nBeams;
    }

    public NbcProc getNbcProc() {
        return nbcProc;
    }

    public void setNbcProc(NbcProc nbcProc) {
        this.nbcProc = nbcProc;
    }
}