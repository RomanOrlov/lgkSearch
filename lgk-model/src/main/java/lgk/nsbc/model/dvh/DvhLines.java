package lgk.nsbc.model.dvh;

import lombok.*;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.NbcDvhLines.NBC_DVH_LINES;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DvhLines implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private Long dvhN;
    private Double dose;
    private Double volume;

    public static DvhLines buildFromRecord(Record record) {
        return builder().n(record.get(NBC_DVH_LINES.N))
                .dvhN(record.get(NBC_DVH_LINES.NBC_DVH_N))
                .dose(record.get(NBC_DVH_LINES.DOSE))
                .volume(record.get(NBC_DVH_LINES.VOLUME))
                .build();
    }
}
