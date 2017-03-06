package lgk.nsbc.backend.entity;

import lgk.nsbc.backend.entity.dictionary.NbcDiag2015;
import lgk.nsbc.backend.entity.dictionary.NbcDiagLoc;
import lgk.nsbc.backend.entity.dictionary.NbcOrganizations;
import lgk.nsbc.backend.entity.dictionary.NbcPatientsDiagnosis;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "NBC_PATIENTS")
public class NbcPatients implements Serializable {
    private static final long serialVersionUID = 1L;

    @OneToOne
    @JoinColumn(name = "BAS_PEOPLE_N")
    private BasPeople basPeople;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DIAGNOSIS")
    private NbcPatientsDiagnosis nbcPatientsDiagnosis;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_DIAG_2015_N")
    private NbcDiag2015 nbcDiag2015;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_DIAG_LOC_N")
    private NbcDiagLoc nbcDiagLoc;

    @OneToOne
    @JoinColumn(name = "NBC_ORGANIZATIONS_N")
    private NbcOrganizations nbcOrganizations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_STAFF_N")
    private NbcStaff nbcStaff;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_PATIENTS_N")
    private List<NbcStud> nbcStudList;

    @Id
    @Column(name = "N")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer n;

    @Column(name = "CASE_HISTORY_NUM")
    private Integer caseHistoryNum;

    @Column(name = "CASE_HISTORY_DATE")
    @Temporal(TemporalType.DATE)
    private Date caseHistoryDate;

    @Column(name = "FULL_DIAGNOSIS")
    private String fullDiagnosis;

    @Column(name = "STATIONARY")
    private Character stationary;

    @Column(name = "ALLERGY")
    private String allergy;

    @Column(name = "INFORMATION_SOURCE")
    private String informationSource;

    @Column(name = "FOLDER")
    private String folder;

    @Column(name = "DISORDER_HISTORY")
    private String disorderHistory;
}