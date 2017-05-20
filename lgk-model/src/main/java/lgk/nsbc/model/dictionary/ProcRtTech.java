package lgk.nsbc.model.dictionary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.NbcProcRtTech.NBC_PROC_RT_TECH;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcRtTech implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private String name;
    private String text;

    public static ProcRtTech buildFromRecord(Record record) {
        return builder().n(record.get(NBC_PROC_RT_TECH.N))
                .name(record.get(NBC_PROC_RT_TECH.NAME))
                .text(record.get(NBC_PROC_RT_TECH.TEXT))
                .build();
    }

    @Override
    public String toString() {
        return text;
    }
}
