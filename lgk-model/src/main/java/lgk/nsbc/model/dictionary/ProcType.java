package lgk.nsbc.model.dictionary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.NbcProcProcType.NBC_PROC_PROC_TYPE;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcType implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private String name;
    private String text;
    private Integer hideStatus;
    private Integer ord;
    private String shorts;

    public static ProcType buildFromRecord(Record record) {
        return builder().n(record.get(NBC_PROC_PROC_TYPE.N))
                .name(record.get(NBC_PROC_PROC_TYPE.NAME))
                .text(record.get(NBC_PROC_PROC_TYPE.TEXT))
                .hideStatus(record.get(NBC_PROC_PROC_TYPE.HIDE_STATUS))
                .ord(record.get(NBC_PROC_PROC_TYPE.ORD))
                .shorts(record.get(NBC_PROC_PROC_TYPE.SHORTS))
                .build();
    }

    @Override
    public String toString() {
        return text;
    }
}
