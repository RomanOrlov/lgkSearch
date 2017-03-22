package lgk.nsbc.backend.entity;

import lgk.nsbc.backend.entity.dictionary.*;
import lgk.nsbc.backend.entity.target.NbcTrgtTreat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Date getProcbegintime() {
        return procbegintime;
    }

    public void setProcbegintime(Date procbegintime) {
        this.procbegintime = procbegintime;
    }

    public Date getProcendtime() {
        return procendtime;
    }

    public void setProcendtime(Date procendtime) {
        this.procendtime = procendtime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getStudComment() {
        return studComment;
    }

    public void setStudComment(String studComment) {
        this.studComment = studComment;
    }

    public Integer getParentProc() {
        return parentProc;
    }

    public void setParentProc(Integer parentProc) {
        this.parentProc = parentProc;
    }

    public NbcPatients getNbcPatients() {
        return nbcPatients;
    }

    public void setNbcPatients(NbcPatients nbcPatients) {
        this.nbcPatients = nbcPatients;
    }

    public NbcOrganizations getNbcOrganizations() {
        return nbcOrganizations;
    }

    public void setNbcOrganizations(NbcOrganizations nbcOrganizations) {
        this.nbcOrganizations = nbcOrganizations;
    }

    public NbcProcProcType getNbcProcProcType() {
        return nbcProcProcType;
    }

    public void setNbcProcProcType(NbcProcProcType nbcProcProcType) {
        this.nbcProcProcType = nbcProcProcType;
    }

    public NbcProcTimeApprox getNbcProcTimeApprox() {
        return nbcProcTimeApprox;
    }

    public void setNbcProcTimeApprox(NbcProcTimeApprox nbcProcTimeApprox) {
        this.nbcProcTimeApprox = nbcProcTimeApprox;
    }

    public NbcProcRtDevice getNbcProcRtDevice() {
        return nbcProcRtDevice;
    }

    public void setNbcProcRtDevice(NbcProcRtDevice nbcProcRtDevice) {
        this.nbcProcRtDevice = nbcProcRtDevice;
    }

    public NbcProcRtTech getNbcProcRtTech() {
        return nbcProcRtTech;
    }

    public void setNbcProcRtTech(NbcProcRtTech nbcProcRtTech) {
        this.nbcProcRtTech = nbcProcRtTech;
    }

    public List<NbcMatrices> getNbcMatrices() {
        return nbcMatrices;
    }

    public void setNbcMatrices(List<NbcMatrices> nbcMatrices) {
        this.nbcMatrices = nbcMatrices;
    }

    public List<NbcTrgtTreat> getNbcTrgtTreats() {
        return nbcTrgtTreats;
    }

    public void setNbcTrgtTreats(List<NbcTrgtTreat> nbcTrgtTreats) {
        this.nbcTrgtTreats = nbcTrgtTreats;
    }
}