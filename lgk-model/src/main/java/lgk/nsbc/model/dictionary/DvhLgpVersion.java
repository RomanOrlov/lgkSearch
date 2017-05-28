package lgk.nsbc.model.dictionary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.DvhLgpVersion.DVH_LGP_VERSION;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DvhLgpVersion implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private String name;
    private String text;

    public static DvhLgpVersion buildFromRecord(Record record) {
        return builder().n(record.get(DVH_LGP_VERSION.N))
                .name(record.get(DVH_LGP_VERSION.NAME))
                .text(record.get(DVH_LGP_VERSION.TEXT))
                .build();
    }

    @Override
    public String toString() {
        return text;
    }
}
