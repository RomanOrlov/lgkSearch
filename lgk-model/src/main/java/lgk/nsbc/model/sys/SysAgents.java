package lgk.nsbc.model.sys;

import lombok.*;
import org.jooq.Record;

import static lgk.nsbc.generated.tables.SysAgents.SYS_AGENTS;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysAgents {
    private Long n;
    private Long op_create;
    private String name;
    private String pid;
    private Long people_n;
    private Long nbc_org_n_default;

    public static SysAgents buildFromRecord(Record record) {
        return builder()
                .n(record.get(SYS_AGENTS.N))
                .name(record.get(SYS_AGENTS.NAME))
                .pid(record.get(SYS_AGENTS.PID))
                .people_n(record.get(SYS_AGENTS.PEOPLE_N))
                .nbc_org_n_default(record.get(SYS_AGENTS.NBC_ORG_N_DEAFULT))
                .build();
    }
}
