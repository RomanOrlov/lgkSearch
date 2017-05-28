package lgk.nsbc.model;

import lombok.*;
import org.jooq.Record;

import java.io.Serializable;
import java.util.Date;

import static lgk.nsbc.generated.tables.People.PEOPLE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(of = {"n", "surname", "name", "patronymic"})
public class People implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private String name;
    private String surname;
    private String patronymic;
    private String sex;
    private Date birthday;
    private String fullName;

    public String getFullName() {
        if (fullName != null) return fullName;
        return surname + " " + name + " " + patronymic;
    }

    public static People buildFromRecord(Record record) {
        return builder()
                .n(record.get(PEOPLE.N))
                .name(record.get(PEOPLE.NAME))
                .surname(record.get(PEOPLE.SURNAME))
                .patronymic(record.get(PEOPLE.PATRONYMIC))
                .sex(record.get(PEOPLE.SEX))
                .birthday(record.get(PEOPLE.BIRTHDAY))
                .build();
    }
}
