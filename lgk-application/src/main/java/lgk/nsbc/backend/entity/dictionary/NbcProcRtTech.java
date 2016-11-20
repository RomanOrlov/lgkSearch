package lgk.nsbc.backend.entity.dictionary;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Техника радиотерапии (IMRT - модуляция интенсивности, статические конформные поля и т.д.)
 */
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

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Integer getOpCreate() {
        return opCreate;
    }

    public void setOpCreate(Integer opCreate) {
        this.opCreate = opCreate;
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
}