package lgk.nsbc.backend.entity.dictionary;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Таблица сокращенных диагнозов
 */
@Setter
@Getter
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

}