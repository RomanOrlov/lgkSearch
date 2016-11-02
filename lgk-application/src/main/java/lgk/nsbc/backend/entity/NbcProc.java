package lgk.nsbc.backend.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "NBC_PROC")
public class NbcProc implements Serializable {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "NBC_STUD_N")
    private NbcStud nbcProc;

    @Id
    @Column(name = "N")
    private Integer n;

    @Column(name = "NBC_PATIENTS_N")
    private Integer nbcPatientsN;

    @Column(name = "PROC_TYPE")
    private Integer procType;

    @Column(name = "PROCBEGINTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date procbegintime;

    @Column(name = "PROCENDTIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date procendtime;

    @Column(name = "TIME_APPROX")
    private BigInteger timeApprox;

    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "RECOMMENDATION")
    private String recommendation;

    @Column(name = "STUD_COMMENT")
    private String studComment;

    @Column(name = "RT_DEVICE")
    private BigInteger rtDevice;

    @Column(name = "RT_TECH")
    private BigInteger rtTech;

    @Column(name = "PARENT_PROC")
    private BigInteger parentProc;

    @Column(name = "NBC_ORGANIZATIONS_N")
    private BigInteger nbcOrganizationsN;

    public NbcProc() {
    }

    public NbcProc(Integer n) {
        this.n = n;
    }

    public NbcProc(Integer n, Integer opCreate, Integer nbcPatientsN) {
        this.n = n;
        this.nbcPatientsN = nbcPatientsN;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public long getNbcPatientsN() {
        return nbcPatientsN;
    }

    public void setNbcPatientsN(Integer nbcPatientsN) {
        this.nbcPatientsN = nbcPatientsN;
    }

    public Integer getProcType() {
        return procType;
    }

    public void setProcType(Integer procType) {
        this.procType = procType;
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

    public BigInteger getTimeApprox() {
        return timeApprox;
    }

    public void setTimeApprox(BigInteger timeApprox) {
        this.timeApprox = timeApprox;
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

    public BigInteger getRtDevice() {
        return rtDevice;
    }

    public void setRtDevice(BigInteger rtDevice) {
        this.rtDevice = rtDevice;
    }

    public BigInteger getRtTech() {
        return rtTech;
    }

    public void setRtTech(BigInteger rtTech) {
        this.rtTech = rtTech;
    }

    public BigInteger getParentProc() {
        return parentProc;
    }

    public void setParentProc(BigInteger parentProc) {
        this.parentProc = parentProc;
    }

    public BigInteger getNbcOrganizationsN() {
        return nbcOrganizationsN;
    }

    public void setNbcOrganizationsN(BigInteger nbcOrganizationsN) {
        this.nbcOrganizationsN = nbcOrganizationsN;
    }

    public NbcStud getNbcProc() {
        return nbcProc;
    }

    public void setNbcProc(NbcStud nbcProc) {
        this.nbcProc = nbcProc;
    }
}

