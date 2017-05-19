package lgk.nsbc.backend.entity;

import lgk.nsbc.backend.entity.dictionary.NbcStudStudyType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "NBC_STUD")
public class NbcStud implements Serializable {
    private static final long serialVersionUID = 1L;

    @OneToOne(fetch = FetchType.LAZY)
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
}