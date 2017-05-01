package lgk.nsbc.model.sys;

import lombok.*;
import org.jooq.Record;

import java.util.Date;

import static lgk.nsbc.generated.tables.SysOperations.SYS_OPERATIONS;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysOperations {
    private Long n;
    private Long session_n;
    private String command_name;
    private Date moment;

    public static SysOperations buildFromRecord(Record record) {
        return builder()
                .n(record.get(SYS_OPERATIONS.N))
                .session_n(record.get(SYS_OPERATIONS.SESSION_N))
                .command_name(record.get(SYS_OPERATIONS.COMMAND_NAME))
                .moment(record.get(SYS_OPERATIONS.MOMENT))
                .build();
    }
}
