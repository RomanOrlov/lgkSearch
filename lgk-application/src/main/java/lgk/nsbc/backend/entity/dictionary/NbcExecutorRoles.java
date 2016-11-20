package lgk.nsbc.backend.entity.dictionary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name = "NBC_EXECUTOR_ROLES")
public class NbcExecutorRoles implements Serializable {
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

