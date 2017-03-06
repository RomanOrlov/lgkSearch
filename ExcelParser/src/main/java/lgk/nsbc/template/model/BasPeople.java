package lgk.nsbc.template.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(of = {"n", "surname", "name", "patronymic"})
public class BasPeople {

    public enum Props {
        n,
        op_create,
        name,
        surname,
        patronymic,
        sex,
        birthday,
        citizenship,
        job,
        obit;

//        @Override
//        public String toString() {
//            return super.toString();
//        }
    }

    private Long n;
    private String name;
    private String surname;
    private String patronymic;
    private String sex;
    private Date birthday;
}
