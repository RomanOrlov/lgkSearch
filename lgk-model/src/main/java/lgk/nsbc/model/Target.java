package lgk.nsbc.model;

import lgk.nsbc.model.dictionary.TargetType;
import lombok.*;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.Target.TARGET;
import static lgk.nsbc.model.dao.dictionary.TargetTypeDao.getTargetTypeMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"n"})
public class Target implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private Long patientsN;
    private String targetName;
    private TargetType targetType;

    public static Target buildFromRecord(Record record) {
        return builder().n(record.get(TARGET.N))
                .patientsN(record.get(TARGET.PATIENTS_N))
                .targetName(record.get(TARGET.TARGETNAME))
                .targetType(getTargetTypeMap().get(record.get(TARGET.TARGETTYPE)))
                .build();
    }

    @Override
    public String toString() {
        return targetName + " " + (targetType == null ? "" : targetType.getText());
    }
}
