package lgk.nsbc.model;

import lombok.*;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.BasSamples.BAS_SAMPLES;

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
        return builder().n(record.get(BAS_SAMPLES.N))
                .code(record.get(BAS_SAMPLES.CODE))
                .name(record.get(BAS_SAMPLES.NAME))
                .sysAgent(record.get(BAS_SAMPLES.SYS_AGENTS_N))
                .description(record.get(BAS_SAMPLES.DESCRIPTION))
                .script(record.get(BAS_SAMPLES.SCRIPT))
                .actuality(record.get(BAS_SAMPLES.ACTUALTY))
                .build();
    }

}
