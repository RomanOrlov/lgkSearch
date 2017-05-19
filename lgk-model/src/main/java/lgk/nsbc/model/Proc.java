package lgk.nsbc.model;

import lombok.*;
import org.jooq.Record;

import java.io.Serializable;
import java.util.Date;

import static lgk.nsbc.generated.tables.NbcProc.NBC_PROC;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proc implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private Long proc_type;
    private Long nbc_stud_n;
    private Long nbc_organizations_n;
    private Date procbegintime;

    public static Proc buildFromRecord(Record record) {
        return builder()
                .n(record.get(NBC_PROC.N))
                .nbc_organizations_n(record.get(NBC_PROC.NBC_ORGANIZATIONS_N))
                .nbc_stud_n(record.get(NBC_PROC.NBC_STUD_N))
                .proc_type(record.get(NBC_PROC.PROC_TYPE))
                .procbegintime(record.get(NBC_PROC.PROCBEGINTIME))
                .build();
    }
}
