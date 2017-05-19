package lgk.nsbc.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
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
}