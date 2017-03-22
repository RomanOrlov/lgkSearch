package lgk.nsbc.backend.entity.dvh;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Алгоритм вычисления дозы
 */
@Setter
@Getter
@Entity
@Table(name = "NBC_DVH_DOSE_ALGORITHM")
public class NbcDvhDoseAlgorithm implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TEXT")
    private String text;

}