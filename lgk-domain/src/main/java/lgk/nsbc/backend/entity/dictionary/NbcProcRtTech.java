package lgk.nsbc.backend.entity.dictionary;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Техника радиотерапии (IMRT - модуляция интенсивности, статические конформные поля и т.д.)
 */
@Setter
@Getter
@Entity
@Table(name = "NBC_PROC_RT_TECH")
public class NbcProcRtTech implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "N")
    private Integer n;

    @Column(name = "OP_CREATE")
    private Integer opCreate;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TEXT")
    private String text;

}