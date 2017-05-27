package lgk.nsbc.model.dictionary;

import lombok.*;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.NbcOrganizations.NBC_ORGANIZATIONS;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private String name;
    private String sign;

    public static Organization buildFromRecord(Record record) {
        return builder()
                .n(record.get(NBC_ORGANIZATIONS.N))
                .name(record.get(NBC_ORGANIZATIONS.NAME))
                .sign(record.get(NBC_ORGANIZATIONS.SIGN))
                .build();
    }

    @Override
    public String toString() {
        return name;
    }
}
