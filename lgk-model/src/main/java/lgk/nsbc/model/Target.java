package lgk.nsbc.model;

import lombok.*;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.Target.TARGET;
import static lgk.nsbc.generated.tables.TargetTargettype.TARGET_TARGETTYPE;

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
    private Long targetType;
    private String targetTypeText;

    public static Target buildFromRecord(Record record) {
        return builder().n(record.get(TARGET.N))
                .patientsN(record.get(TARGET.PATIENTS_N))
                .targetName(record.get(TARGET.TARGETNAME))
                .targetType(record.get(TARGET.TARGETTYPE))
                .targetTypeText(record.get(TARGET_TARGETTYPE.TEXT))
                .build();
    }

    @Override
    public String toString() {
        return targetName + " " + targetTypeText;
    }
}
