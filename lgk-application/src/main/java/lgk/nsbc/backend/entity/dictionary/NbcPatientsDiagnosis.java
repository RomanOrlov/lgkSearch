package lgk.nsbc.backend.entity.dictionary;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Таблица сокращенных диагнозов
 */
@Entity
@Table(name = "NBC_PATIENTS_DIAGNOSIS")
public class NbcPatientsDiagnosis implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @Column(name = "UP_N")
    private Integer upN;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "USEFUL")
    private Character useful;

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Integer getUpN() {
        return upN;
    }

    public void setUpN(Integer upN) {
        this.upN = upN;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Character getUseful() {
        return useful;
    }

    public void setUseful(Character useful) {
        this.useful = useful;
    }
}