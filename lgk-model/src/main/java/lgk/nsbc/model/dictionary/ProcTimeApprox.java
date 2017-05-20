package lgk.nsbc.model.dictionary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.NbcProcTimeApprox.NBC_PROC_TIME_APPROX;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcTimeApprox implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private String name;
    private String text;
    private Integer hideStatus;
    private Integer ord;

    public static ProcTimeApprox buildFromRecord(Record record) {
        return builder().n(record.get(NBC_PROC_TIME_APPROX.N))
                .name(record.get(NBC_PROC_TIME_APPROX.NAME))
                .text(record.get(NBC_PROC_TIME_APPROX.TEXT))
                .hideStatus(record.get(NBC_PROC_TIME_APPROX.HIDE_STATUS))
                .ord(record.get(NBC_PROC_TIME_APPROX.ORD))
                .build();
    }

    @Override
    public String toString() {
        return text;
    }
}
