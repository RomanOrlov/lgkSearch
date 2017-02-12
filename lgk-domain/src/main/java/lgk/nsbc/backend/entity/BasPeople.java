package lgk.nsbc.backend.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Character getSex() {
        return sex;
    }

    public void setSex(Character sex) {
        this.sex = sex;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Date getObit() {
        return obit;
    }

    public void setObit(Date obit) {
        this.obit = obit;
    }

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