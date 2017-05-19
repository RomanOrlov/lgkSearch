package lgk.nsbc.backend.entity.histology;

import lgk.nsbc.backend.entity.NbcPatients;
import lgk.nsbc.backend.entity.NbcStud;
import lgk.nsbc.backend.entity.dictionary.NbcPatientsDiagnosis;
import lgk.nsbc.backend.entity.target.NbcTarget;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
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

}