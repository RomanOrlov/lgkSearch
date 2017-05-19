package lgk.nsbc.model.dictionary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.NbcDvhDataVersion.NBC_DVH_DATA_VERSION;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DvhDataVersion implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private String name;
    private String text;

    public static DvhDataVersion buildFromRecord(Record record) {
        return builder().n(record.get(NBC_DVH_DATA_VERSION.N))
                .name(record.get(NBC_DVH_DATA_VERSION.NAME))
                .text(record.get(NBC_DVH_DATA_VERSION.TEXT))
                .build();
    }

    @Override
    public String toString() {
        return text;
    }
}
