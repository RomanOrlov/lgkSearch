package lgk.nsbc.model;

import lombok.*;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.Samples.SAMPLES;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sample implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private String code;
    private String name;
    private Long sysAgent;
    private String description;
    private String script;
    private String actuality;

    public static Sample buildFromRecord(Record record) {
        return builder().n(record.get(SAMPLES.N))
                .code(record.get(SAMPLES.CODE))
                .name(record.get(SAMPLES.NAME))
                .sysAgent(record.get(SAMPLES.SYS_AGENTS_N))
                .description(record.get(SAMPLES.DESCRIPTION))
                .script(record.get(SAMPLES.SCRIPT))
                .actuality(record.get(SAMPLES.ACTUALTY))
                .build();
    }

}
