package lgk.nsbc.backend.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "NBC_STUD")
public class NbcStud implements Serializable {
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_PATIENTS_N")
    private NbcPatients nbcPatients;

    @ManyToOne
    @JoinColumn(name = "STUDY_TYPE")
    @PrimaryKeyJoinColumn
    private NbcStudStudyType nbcStudStudyType;

    @OneToMany(mappedBy = "nbcProc",fetch = FetchType.LAZY)
    private List<NbcProc> nbcProcs;

    @Id
    @Column(name = "N")
    private Integer n;

    @Column(name = "OP_CREATE")
    private Integer opCreate;

    @Column(name = "NBC_PROCEDURES_N")
    private Integer nbcProceduresN;

    @Column(name = "STUDYDATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date studydatetime;

    @Column(name = "NEW_TARGETS")
    private Integer newTargets;

    @Column(name = "DICOM_STUDY_ID")
    private String dicomStudyId;

    @Column(name = "DICOM_STUDY_DATE")
    private String dicomStudyDate;

    @Column(name = "DICOM_SERIES_NUM")
    private String dicomSeriesNum;

    @Column(name = "DICOM_PATIENT_NAME")
    private String dicomPatientName;

    @Column(name = "DICOM_BIRTH_DATE")
    private String dicomBirthDate;

    public NbcStud() {
    }

    public NbcStud(Integer n) {
        this.n = n;
    }

    public NbcStud(Integer n, Integer opCreate) {
        this.n = n;
        this.opCreate = opCreate;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Integer getOpCreate() {
        return opCreate;
    }

    public void setOpCreate(Integer opCreate) {
        this.opCreate = opCreate;
    }

    public Integer getNbcProceduresN() {
        return nbcProceduresN;
    }

    public void setNbcProceduresN(Integer nbcProceduresN) {
        this.nbcProceduresN = nbcProceduresN;
    }

    public Date getStudydatetime() {
        return studydatetime;
    }

    public void setStudydatetime(Date studydatetime) {
        this.studydatetime = studydatetime;
    }

    public Integer getNewTargets() {
        return newTargets;
    }

    public void setNewTargets(Integer newTargets) {
        this.newTargets = newTargets;
    }

    public String getDicomStudyId() {
        return dicomStudyId;
    }

    public void setDicomStudyId(String dicomStudyId) {
        this.dicomStudyId = dicomStudyId;
    }

    public String getDicomStudyDate() {
        return dicomStudyDate;
    }

    public void setDicomStudyDate(String dicomStudyDate) {
        this.dicomStudyDate = dicomStudyDate;
    }

    public String getDicomSeriesNum() {
        return dicomSeriesNum;
    }

    public void setDicomSeriesNum(String dicomSeriesNum) {
        this.dicomSeriesNum = dicomSeriesNum;
    }

    public String getDicomPatientName() {
        return dicomPatientName;
    }

    public void setDicomPatientName(String dicomPatientName) {
        this.dicomPatientName = dicomPatientName;
    }

    public String getDicomBirthDate() {
        return dicomBirthDate;
    }

    public void setDicomBirthDate(String dicomBirthDate) {
        this.dicomBirthDate = dicomBirthDate;
    }

    public NbcPatients getNbcPatients() {
        return nbcPatients;
    }

    public void setNbcPatients(NbcPatients nbcPatients) {
        this.nbcPatients = nbcPatients;
    }

    public NbcStudStudyType getNbcStudStudyType() {
        return nbcStudStudyType;
    }

    public void setNbcStudStudyType(NbcStudStudyType nbcStudStudyType) {
        this.nbcStudStudyType = nbcStudStudyType;
    }

    public List<NbcProc> getNbcProcs() {
        return nbcProcs;
    }

    public void setNbcProcs(List<NbcProc> nbcProcs) {
        this.nbcProcs = nbcProcs;
    }
}

