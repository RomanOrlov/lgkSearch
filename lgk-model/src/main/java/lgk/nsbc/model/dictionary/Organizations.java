package lgk.nsbc.model.dictionary;

import lombok.*;
import org.jooq.Record;

import static lgk.nsbc.generated.tables.NbcOrganizations.NBC_ORGANIZATIONS;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organizations {
    private Long n;
    private String name;

    public static Organizations buildFromRecord(Record record) {
        return builder()
                .n(record.get(NBC_ORGANIZATIONS.N))
                .name(record.get(NBC_ORGANIZATIONS.NAME))
                .build();
    }
}
