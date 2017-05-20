package lgk.nsbc.backend.entity.viiinerve;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Сохранность слуха
 */
@Setter
@Getter
@Entity
@Table(name = "NBC_VIIIN_1_HEARING_DISFUNC")
public class NbcViiinHearingDisfunc implements Serializable {
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