package lgk.nsbc.model;

import lombok.*;
import org.jooq.Record;

import static lgk.nsbc.generated.tables.NbcTarget.NBC_TARGET;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"n"})
public class NbcTarget {
    private Long n;
    private Long nbc_patients_n;
    private String targetname;
    private Long targettype;

    public static NbcTarget buildFromRecord(Record record) {
        return builder().n(record.get(NBC_TARGET.N))
                .nbc_patients_n(record.get(NBC_TARGET.NBC_PATIENTS_N))
                .targetname(record.get(NBC_TARGET.TARGETNAME))
                .targettype(record.get(NBC_TARGET.TARGETTYPE))
                .build();
    }

    @Override
    public String toString() {
        return targetname;
    }
}
