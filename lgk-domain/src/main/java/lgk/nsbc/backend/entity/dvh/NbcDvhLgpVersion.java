package lgk.nsbc.backend.entity.dvh;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Версия ПО (LGP), рассчитывающего DVH
 */
@Setter
@Getter
@Entity
@Table(name = "NBC_DVH_LGP_VERSION")
public class NbcDvhLgpVersion implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TEXT")
    private String text;

}