package lgk.nsbc.backend.entity;

import lgk.nsbc.backend.entity.dictionary.NbcStudStudyType;

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
    private NbcPatients nbcPatient;

    @OneToOne
    @JoinColumn(name = "STUDY_TYPE")
    private NbcStudStudyType nbcStudStudyType;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_PROCEDURES_N")
    private List<NbcProc> nbcProcedures;

    @Id
    @Column(name = "N")
    private Integer n;

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

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
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

    public NbcPatients getNbcPatient() {
        return nbcPatient;
    }

    public void setNbcPatient(NbcPatients nbcPatient) {
        this.nbcPatient = nbcPatient;
    }

    public NbcStudStudyType getNbcStudStudyType() {
        return nbcStudStudyType;
    }

    public void setNbcStudStudyType(NbcStudStudyType nbcStudStudyType) {
        this.nbcStudStudyType = nbcStudStudyType;
    }

    public List<NbcProc> getNbcProcedures() {
        return nbcProcedures;
    }

    public void setNbcProcedures(List<NbcProc> nbcProcedures) {
        this.nbcProcedures = nbcProcedures;
    }
}