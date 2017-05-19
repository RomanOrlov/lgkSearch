package lgk.nsbc.backend.entity.viiinerve;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Состояние слуха по шкале Гарднера-Робертсона
 */
@Setter
@Getter
@Entity
@Table(name = "NBC_VIIIN_1_GARD_ROBERT")
public class NbcViiinGardRobert implements Serializable {
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