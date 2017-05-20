package lgk.nsbc.backend.entity.vnerve;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Онемение
 */
@Setter
@Getter
@Entity
@Table(name = "NBC_VN_1_HYPOSTESY")
public class NbcVnHypostesy implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TEXT")
    private String text;

}