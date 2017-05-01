package lgk.nsbc.model;

import lombok.*;
import org.jooq.Record;

import static lgk.nsbc.generated.tables.NbcTarget.NBC_TARGET;
import static lgk.nsbc.generated.tables.NbcTargetTargettype.NBC_TARGET_TARGETTYPE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"n"})
public class NbcTarget {
    private Long n;
    private Long nbc_patients_n;
    private String targetName;
    private Long targetType;
    private String targetTypeText;

    public static NbcTarget buildFromRecord(Record record) {
        return builder().n(record.get(NBC_TARGET.N))
                .nbc_patients_n(record.get(NBC_TARGET.NBC_PATIENTS_N))
                .targetName(record.get(NBC_TARGET.TARGETNAME))
                .targetType(record.get(NBC_TARGET.TARGETTYPE))
                .targetTypeText(record.get(NBC_TARGET_TARGETTYPE.TEXT))
                .build();
    }

    @Override
    public String toString() {
        return targetName+ " " + targetTypeText ;
    }
}
