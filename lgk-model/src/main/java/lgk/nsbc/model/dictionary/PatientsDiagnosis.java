package lgk.nsbc.model.dictionary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jooq.Record;

import static lgk.nsbc.generated.tables.NbcPatientsDiagnosis.NBC_PATIENTS_DIAGNOSIS;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientsDiagnosis {
    private Long n;
    private Long upN;
    private String name;
    private String text;
    private String useful;

    public static PatientsDiagnosis buildFromRecord(Record record) {
        return builder().n(record.get(NBC_PATIENTS_DIAGNOSIS.N))
                .name(record.get(NBC_PATIENTS_DIAGNOSIS.NAME))
                .text(record.get(NBC_PATIENTS_DIAGNOSIS.TEXT))
                .build();
    }
}
