package lgk.nsbc.model;

import lombok.*;
import org.jooq.Record;

import java.util.Date;

import static lgk.nsbc.generated.tables.BasPeople.BAS_PEOPLE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(of = {"n", "surname", "name", "patronymic"})
public class People {
    private Long n;
    private String name;
    private String surname;
    private String patronymic;
    private String sex;
    private Date birthday;

    public static People buildFromRecord(Record record) {
        return builder()
                .n(record.get(BAS_PEOPLE.N))
                .name(record.get(BAS_PEOPLE.NAME))
                .surname(record.get(BAS_PEOPLE.SURNAME))
                .patronymic(record.get(BAS_PEOPLE.PATRONYMIC))
                .sex(record.get(BAS_PEOPLE.SEX))
                .birthday(record.get(BAS_PEOPLE.BIRTHDAY))
                .build();
    }
}
