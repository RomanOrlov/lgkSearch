package lgk.nsbc.backend.entity.dictionary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "NBC_PROC_PROC_TYPE")
public class NbcProcProcType implements Serializable {
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

    @Column(name = "HIDE_STATUS")
    private Integer hideStatus;

    @Column(name = "ORD")
    private Integer ord;

    @Column(name = "SHORTS")
    private String shorts;

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

    public Integer getHideStatus() {
        return hideStatus;
    }

    public void setHideStatus(Integer hideStatus) {
        this.hideStatus = hideStatus;
    }

    public Integer getOrd() {
        return ord;
    }

    public void setOrd(Integer ord) {
        this.ord = ord;
    }

    public String getShorts() {
        return shorts;
    }

    public void setShorts(String shorts) {
        this.shorts = shorts;
    }
}