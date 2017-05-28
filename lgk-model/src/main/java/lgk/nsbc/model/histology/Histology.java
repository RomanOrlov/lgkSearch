package lgk.nsbc.model.histology;

import lombok.*;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.Histology.HISTOLOGY;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Histology implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private Long studN;
    private Long diagVerif;
    private String histVerifBurd;
    private Long patientsDiagnosisN;
    private Long cancerHistologyCon;
    private Long oncoDiagCon;
    private String commentary;

    private Long patientsN;
    private Long targetN;
    private Double ki67From;
    private Double ki67To;
    private Integer her2neu;
    private Long fish;
    private Integer erB;
    private Long er;
    private Integer pgB;
    private Long pg;

    public static Histology buildFromRecord(Record record) {
        return builder().n(record.get(HISTOLOGY.N))
                .studN(record.get(HISTOLOGY.STUD_N))
                .diagVerif(record.get(HISTOLOGY.DIAG_VERIF))
                .histVerifBurd(record.get(HISTOLOGY.HIST_VERIF_BURD))
                .patientsDiagnosisN(record.get(HISTOLOGY.PATIENTS_DIAGNOSIS_N))
                .cancerHistologyCon(record.get(HISTOLOGY.CANCER_HISTOLOGY_CON))
                .oncoDiagCon(record.get(HISTOLOGY.ONCO_DIAG_CON))
                .commentary(record.get(HISTOLOGY.COMMENTARY))
                .patientsN(record.get(HISTOLOGY.PATIENTS_N))
                .targetN(record.get(HISTOLOGY.TARGET_N))
                .ki67From(record.get(HISTOLOGY.KI67_FROM))
                .ki67To(record.get(HISTOLOGY.KI67_TO))
                .her2neu(record.get(HISTOLOGY.HER2NEU))
                .fish(record.get(HISTOLOGY.FISH))
                .erB(record.get(HISTOLOGY.ER_B))
                .er(record.get(HISTOLOGY.ER))
                .pgB(record.get(HISTOLOGY.PG_B))
                .pg(record.get(HISTOLOGY.PG))
                .build();
    }
}
