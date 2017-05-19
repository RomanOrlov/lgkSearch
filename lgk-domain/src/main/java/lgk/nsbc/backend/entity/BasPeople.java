package lgk.nsbc.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "BAS_PEOPLE")
public class BasPeople implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer n;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PATRONYMIC")
    private String patronymic;

    @Column(name = "BIRTHDAY")
    @Temporal(TemporalType.DATE)
    private Date birthday;

    @Column(name = "SEX")
    private Character sex;

    @Column(name = "CITIZENSHIP")
    private String citizenship;

    @Column(name = "JOB")
    private String job;

    @Column(name = "OBIT")
    @Temporal(TemporalType.DATE)
    private Date obit;

    @Override
    public String toString() {
        return "BasPeople{" +
                "birthday=" + birthday +
                ", n=" + n +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", sex=" + sex +
                ", citizenship='" + citizenship + '\'' +
                ", job='" + job + '\'' +
                ", obit=" + obit +
                '}';
    }
}