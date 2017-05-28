package lgk.nsbc.model;

import lgk.nsbc.model.dao.dictionary.StudTypeDao;
import lgk.nsbc.model.dictionary.StudType;
import lombok.*;
import org.jooq.Record;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

import static lgk.nsbc.generated.tables.Stud.STUD;

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
    private Date studyDateTime;
    private StudType studType;
    private Long patientsN;
    private Long proceduresN;

    public static Optional<Stud> buildFromRecord(Record record) {
        if (record == null) return Optional.empty();
        return Optional.of(builder()
                .n(record.get(STUD.N))
                .patientsN(record.get(STUD.PATIENTS_N))
                .proceduresN(record.get(STUD.PROCEDURES_N))
                .studType(StudTypeDao.getStudTypeMap().get(record.get(STUD.STUDY_TYPE)))
                .studyDateTime(record.get(STUD.STUDYDATETIME))
                .build());
    }
}
