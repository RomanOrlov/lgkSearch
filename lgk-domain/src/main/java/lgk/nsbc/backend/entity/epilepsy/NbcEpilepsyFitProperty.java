package lgk.nsbc.backend.entity.epilepsy;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Описание характера приступов
 */
@Setter
@Getter
@Entity
@Table(name = "NBC_EPILEPSY_1_FIT_PROPERTY")
public class NbcEpilepsyFitProperty implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "AMOUNT")
    private Integer amount;

}