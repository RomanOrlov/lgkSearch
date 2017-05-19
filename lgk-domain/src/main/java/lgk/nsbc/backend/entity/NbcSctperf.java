package lgk.nsbc.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "NBC_SCTPERF")
public class NbcSctperf implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_STUD_N")
    private NbcStud nbcStud;

    @Column(name = "CBF")
    private Double cbf;

    @Column(name = "CBV")
    private Double cbv;

    @Column(name = "MTT")
    private Double mtt;

    @Column(name = "PS")
    private Double ps;

}