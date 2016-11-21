package lgk.nsbc.backend.entity.histology;

import lgk.nsbc.backend.entity.NbcPatients;
import lgk.nsbc.backend.entity.NbcStud;
import lgk.nsbc.backend.entity.target.NbcTarget;
import lgk.nsbc.backend.entity.dictionary.NbcPatientsDiagnosis;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "NBC_HISTOLOGY_1")
public class NbcHistology implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_STUD_N")
    private NbcStud nbcStud;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_TARGET_N")
    private NbcTarget nbcTarget;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_PATIENTS_N")
    private NbcPatients nbcPatients;

    @OneToOne
    @JoinColumn(name = "DIAG_VERIF")
    private NbcHistologyDiagVerif nbcHistologyDiagVerif;

    @Column(name = "HIST_VERIF_BURD")
    private Character histVerifBurd;

    @OneToOne
    @JoinColumn(name = "NBC_PATIENTS_DIAGNOSIS_N")
    private NbcPatientsDiagnosis nbcPatientsDiagnosis;

    @OneToOne
    @JoinColumn(name = "CANCER_HISTOLOGY_1_CON")
    private NbcCancerHistologyCon nbcCancerHistologyCon;

    @OneToOne
    @JoinColumn(name = "ONCO_DIAG_1_CON")
    private NbcOncoDiagCon nbcOncoDiagCon;

    @Column(name = "COMMENTARY")
    private String commentary;

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public NbcStud getNbcStud() {
        return nbcStud;
    }

    public void setNbcStud(NbcStud nbcStud) {
        this.nbcStud = nbcStud;
    }

    public NbcTarget getNbcTarget() {
        return nbcTarget;
    }

    public void setNbcTarget(NbcTarget nbcTarget) {
        this.nbcTarget = nbcTarget;
    }

    public NbcPatients getNbcPatients() {
        return nbcPatients;
    }

    public void setNbcPatients(NbcPatients nbcPatients) {
        this.nbcPatients = nbcPatients;
    }

    public NbcHistologyDiagVerif getNbcHistologyDiagVerif() {
        return nbcHistologyDiagVerif;
    }

    public void setNbcHistologyDiagVerif(NbcHistologyDiagVerif nbcHistologyDiagVerif) {
        this.nbcHistologyDiagVerif = nbcHistologyDiagVerif;
    }

    public Character getHistVerifBurd() {
        return histVerifBurd;
    }

    public void setHistVerifBurd(Character histVerifBurd) {
        this.histVerifBurd = histVerifBurd;
    }

    public NbcPatientsDiagnosis getNbcPatientsDiagnosis() {
        return nbcPatientsDiagnosis;
    }

    public void setNbcPatientsDiagnosis(NbcPatientsDiagnosis nbcPatientsDiagnosis) {
        this.nbcPatientsDiagnosis = nbcPatientsDiagnosis;
    }

    public NbcCancerHistologyCon getNbcCancerHistologyCon() {
        return nbcCancerHistologyCon;
    }

    public void setNbcCancerHistologyCon(NbcCancerHistologyCon nbcCancerHistologyCon) {
        this.nbcCancerHistologyCon = nbcCancerHistologyCon;
    }

    public NbcOncoDiagCon getNbcOncoDiagCon() {
        return nbcOncoDiagCon;
    }

    public void setNbcOncoDiagCon(NbcOncoDiagCon nbcOncoDiagCon) {
        this.nbcOncoDiagCon = nbcOncoDiagCon;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }
}