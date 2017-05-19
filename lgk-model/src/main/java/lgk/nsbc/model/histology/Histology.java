package lgk.nsbc.model.histology;

import lombok.*;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.NbcHistology_1.NBC_HISTOLOGY_1;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Histology implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private Long nbcStudN;
    private Long diagVerif;
    private String histVerifBurd;
    private Long nbcPatientsDiagnosisN;
    private Long cancerHistologyCon;
    private Long oncoDiagCon;
    private String commentary;

    private Long nbcPatientsN;
    private Long nbcTargetN;
    private Double ki67From;
    private Double ki67To;
    private Integer her2neu;
    private Long fish;
    private Integer erB;
    private Long er;
    private Integer pgB;
    private Long pg;

    public static Histology buildFromRecord(Record record) {
        return builder().n(record.get(NBC_HISTOLOGY_1.N))
                .nbcStudN(record.get(NBC_HISTOLOGY_1.NBC_STUD_N))
                .diagVerif(record.get(NBC_HISTOLOGY_1.DIAG_VERIF))
                .histVerifBurd(record.get(NBC_HISTOLOGY_1.HIST_VERIF_BURD))
                .nbcPatientsDiagnosisN(record.get(NBC_HISTOLOGY_1.NBC_PATIENTS_DIAGNOSIS_N))
                .cancerHistologyCon(record.get(NBC_HISTOLOGY_1.CANCER_HISTOLOGY_1_CON))
                .oncoDiagCon(record.get(NBC_HISTOLOGY_1.ONCO_DIAG_1_CON))
                .commentary(record.get(NBC_HISTOLOGY_1.COMMENTARY))
                .nbcPatientsN(record.get(NBC_HISTOLOGY_1.NBC_PATIENTS_N))
                .nbcTargetN(record.get(NBC_HISTOLOGY_1.NBC_TARGET_N))
                .ki67From(record.get(NBC_HISTOLOGY_1.KI67_FROM))
                .ki67To(record.get(NBC_HISTOLOGY_1.KI67_TO))
                .her2neu(record.get(NBC_HISTOLOGY_1.HER2NEU))
                .fish(record.get(NBC_HISTOLOGY_1.FISH))
                .erB(record.get(NBC_HISTOLOGY_1.ER_B))
                .er(record.get(NBC_HISTOLOGY_1.ER))
                .pgB(record.get(NBC_HISTOLOGY_1.PG_B))
                .pg(record.get(NBC_HISTOLOGY_1.PG))
                .build();
    }
}
