package lgk.nsbc.model.dictionary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.ProcRtDevice.PROC_RT_DEVICE;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcRtDevice implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private String name;
    private String text;

    public static ProcRtDevice buildFromRecord(Record record) {
        return builder().n(record.get(PROC_RT_DEVICE.N))
                .name(record.get(PROC_RT_DEVICE.NAME))
                .text(record.get(PROC_RT_DEVICE.TEXT))
                .build();
    }

    @Override
    public String toString() {
        return text;
    }
}
