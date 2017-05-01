package lgk.nsbc.model;

import lombok.*;
import org.jooq.Record;

import static lgk.nsbc.generated.tables.NbcOrganizations.NBC_ORGANIZATIONS;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NbcOrganizations {
    private Long n;
    private String name;

    public static NbcOrganizations buildFromRecord(Record record) {
        return builder()
                .n(record.get(NBC_ORGANIZATIONS.N))
                .name(record.get(NBC_ORGANIZATIONS.NAME))
                .build();
    }
}
