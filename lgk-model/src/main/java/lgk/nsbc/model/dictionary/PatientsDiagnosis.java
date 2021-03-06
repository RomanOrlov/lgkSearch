package lgk.nsbc.model.dictionary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.PatientsDiagnosis.PATIENTS_DIAGNOSIS;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientsDiagnosis implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private Long upN;
    private String name;
    private String text;
    private String useful;

    public static PatientsDiagnosis buildFromRecord(Record record) {
        return builder().n(record.get(PATIENTS_DIAGNOSIS.N))
                .name(record.get(PATIENTS_DIAGNOSIS.NAME))
                .text(record.get(PATIENTS_DIAGNOSIS.TEXT))
                .build();
    }
}
