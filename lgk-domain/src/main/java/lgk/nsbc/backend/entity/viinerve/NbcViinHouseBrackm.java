package lgk.nsbc.backend.entity.viinerve;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Шкала House-Brackmann
 */
@Setter
@Getter
@Entity
@Table(name = "NBC_VIIN_1_HOUSE_BRACKM")
public class NbcViinHouseBrackm implements Serializable {
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