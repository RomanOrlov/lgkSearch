package lgk.nsbc.model;

import lombok.*;
import org.jooq.Record;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

import static lgk.nsbc.generated.tables.NbcStud.NBC_STUD;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stud implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private Patients patients;
    private Proc proc;
    private Date studydatetime;
    private Long study_type;
    private Long nbc_patients_n;
    private Long nbc_procedures_n;

    public static Optional<Stud> buildFromRecord(Record record) {
        if (record == null) return Optional.empty();
        return Optional.of(builder()
                .n(record.get(NBC_STUD.N))
                .nbc_patients_n(record.get(NBC_STUD.NBC_PATIENTS_N))
                .nbc_procedures_n(record.get(NBC_STUD.NBC_PROCEDURES_N))
                .study_type(record.get(NBC_STUD.STUDY_TYPE))
                .studydatetime(record.get(NBC_STUD.STUDYDATETIME))
                .build());
    }
}
