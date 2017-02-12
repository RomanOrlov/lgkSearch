package lgk.nsbc.backend.entity;

import lgk.nsbc.backend.entity.dictionary.NbcDiag2015;
import lgk.nsbc.backend.entity.dictionary.NbcDiagLoc;
import lgk.nsbc.backend.entity.dictionary.NbcOrganizations;
import lgk.nsbc.backend.entity.dictionary.NbcPatientsDiagnosis;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "NBC_PATIENTS")
public class NbcPatients implements Serializable {
    private static final long serialVersionUID = 1L;

    @OneToOne
    @JoinColumn(name = "BAS_PEOPLE_N")
    private BasPeople basPeople;

    @OneToOne
    @JoinColumn(name = "DIAGNOSIS")
    private NbcPatientsDiagnosis nbcPatientsDiagnosis;

    @OneToOne
    @JoinColumn(name = "NBC_DIAG_2015_N")
    private NbcDiag2015 nbcDiag2015;

    @OneToOne
    @JoinColumn(name = "NBC_DIAG_LOC_N")
    private NbcDiagLoc nbcDiagLoc;

    @OneToOne(fetch = FetchType.LAZY)
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

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Integer getCaseHistoryNum() {
        return caseHistoryNum;
    }

    public void setCaseHistoryNum(Integer caseHistoryNum) {
        this.caseHistoryNum = caseHistoryNum;
    }

    public Date getCaseHistoryDate() {
        return caseHistoryDate;
    }

    public void setCaseHistoryDate(Date caseHistoryDate) {
        this.caseHistoryDate = caseHistoryDate;
    }

    public String getFullDiagnosis() {
        return fullDiagnosis;
    }

    public void setFullDiagnosis(String fullDiagnosis) {
        this.fullDiagnosis = fullDiagnosis;
    }

    public Character getStationary() {
        return stationary;
    }

    public void setStationary(Character stationary) {
        this.stationary = stationary;
    }

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public String getInformationSource() {
        return informationSource;
    }

    public void setInformationSource(String informationSource) {
        this.informationSource = informationSource;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getDisorderHistory() {
        return disorderHistory;
    }

    public void setDisorderHistory(String disorderHistory) {
        this.disorderHistory = disorderHistory;
    }

    public BasPeople getBasPeople() {
        return basPeople;
    }

    public void setBasPeople(BasPeople basPeople) {
        this.basPeople = basPeople;
    }

    public NbcPatientsDiagnosis getNbcPatientsDiagnosis() {
        return nbcPatientsDiagnosis;
    }

    public void setNbcPatientsDiagnosis(NbcPatientsDiagnosis nbcPatientsDiagnosis) {
        this.nbcPatientsDiagnosis = nbcPatientsDiagnosis;
    }

    public NbcOrganizations getNbcOrganizations() {
        return nbcOrganizations;
    }

    public void setNbcOrganizations(NbcOrganizations nbcOrganizations) {
        this.nbcOrganizations = nbcOrganizations;
    }

    public NbcDiag2015 getNbcDiag2015() {
        return nbcDiag2015;
    }

    public void setNbcDiag2015(NbcDiag2015 nbcDiag2015) {
        this.nbcDiag2015 = nbcDiag2015;
    }

    public NbcDiagLoc getNbcDiagLoc() {
        return nbcDiagLoc;
    }

    public void setNbcDiagLoc(NbcDiagLoc nbcDiagLoc) {
        this.nbcDiagLoc = nbcDiagLoc;
    }

    public NbcStaff getNbcStaff() {
        return nbcStaff;
    }

    public void setNbcStaff(NbcStaff nbcStaff) {
        this.nbcStaff = nbcStaff;
    }

    public List<NbcStud> getNbcStudList() {
        return nbcStudList;
    }

    public void setNbcStudList(List<NbcStud> nbcStudList) {
        this.nbcStudList = nbcStudList;
    }
}