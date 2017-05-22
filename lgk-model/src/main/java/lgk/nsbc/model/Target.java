package lgk.nsbc.model;

import lombok.*;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.NbcTarget.NBC_TARGET;
import static lgk.nsbc.generated.tables.NbcTargetTargettype.NBC_TARGET_TARGETTYPE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"n"})
public class Target implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private Long nbcPatientsN;
    private String targetName;
    private Long targetType;
    private String targetTypeText;

    public static Target buildFromRecord(Record record) {
        return builder().n(record.get(NBC_TARGET.N))
                .nbcPatientsN(record.get(NBC_TARGET.NBC_PATIENTS_N))
                .targetName(record.get(NBC_TARGET.TARGETNAME))
                .targetType(record.get(NBC_TARGET.TARGETTYPE))
                .targetTypeText(record.get(NBC_TARGET_TARGETTYPE.TEXT))
                .build();
    }

    @Override
    public String toString() {
        return targetName + " " + targetTypeText;
    }
}
