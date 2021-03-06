package lgk.nsbc.model.sys;

import lombok.*;
import org.jooq.Record;

import java.io.Serializable;
import java.util.Date;

import static lgk.nsbc.generated.tables.SysSessions.SYS_SESSIONS;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysSessions implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private String sid;
    private Long agent_n;
    private Date opened;

    public static SysSessions buildFromRecord(Record record) {
        return builder()
                .n(record.get(SYS_SESSIONS.N))
                .sid(record.get(SYS_SESSIONS.SID))
                .agent_n(record.get(SYS_SESSIONS.AGENT_N))
                .opened(record.get(SYS_SESSIONS.OPENED))
                .build();
    }
}
