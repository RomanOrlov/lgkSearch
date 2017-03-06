package lgk.nsbc.template.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@ToString(of = {"n", "surname","name","patronymic"})
public class BasPeople {

    public enum Props {
        n, op_create, name, surname, patronymic, sex, birthday, citizenship, job, obit
    }

    private Long n;
    private String name;
    private String surname;
    private String patronymic;
    private String sex;
    private Date birthday;
}
