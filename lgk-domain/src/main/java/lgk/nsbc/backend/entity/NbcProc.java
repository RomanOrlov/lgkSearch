package lgk.nsbc.backend.entity;

import lgk.nsbc.backend.entity.dictionary.*;
import lgk.nsbc.backend.entity.target.NbcTrgtTreat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "NBC_PROC")
public class NbcProc implements Serializable {
    private static final long serialVersionUID = 1L;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_PATIENTS_N")
    private NbcPatients nbcPatients;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_ORGANIZATIONS_N")
    private NbcOrganizations nbcOrganizations;

    @OneToOne
    @JoinColumn(name = "PROC_TYPE")
    private NbcProcProcType nbcProcProcType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIME_APPROX")
    private NbcProcTimeApprox nbcProcTimeApprox;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RT_DEVICE")
    private NbcProcRtDevice nbcProcRtDevice;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RT_TECH")
    private NbcProcRtTech nbcProcRtTech;

    @OneToMany(fetch = FetchType.LAZY)
    private List<NbcMatrices> nbcMatrices;

    @OneToMany(mappedBy = "nbcProc", fetch = FetchType.LAZY)
    private List<NbcTrgtTreat> nbcTrgtTreats;

    @Id
    @Column(name = "N")
    private Integer n;

    @Column(name = "PROCBEGINTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date procbegintime;

    @Column(name = "PROCENDTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date procendtime;

    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "RECOMMENDATION")
    private String recommendation;

    @Column(name = "STUD_COMMENT")
    private String studComment;

    @Column(name = "PARENT_PROC")
    private Integer parentProc;

}