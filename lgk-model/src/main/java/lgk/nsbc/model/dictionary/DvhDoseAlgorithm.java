package lgk.nsbc.model.dictionary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.NbcDvhDoseAlgorithm.NBC_DVH_DOSE_ALGORITHM;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DvhDoseAlgorithm implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private String name;
    private String text;

    public static DvhDoseAlgorithm buildFromRecord(Record record) {
        return builder().n(record.get(NBC_DVH_DOSE_ALGORITHM.N))
                .name(record.get(NBC_DVH_DOSE_ALGORITHM.NAME))
                .text(record.get(NBC_DVH_DOSE_ALGORITHM.TEXT))
                .build();
    }

    @Override
    public String toString() {
        return text;
    }
}
