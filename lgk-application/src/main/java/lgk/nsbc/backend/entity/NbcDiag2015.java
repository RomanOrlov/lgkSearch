package lgk.nsbc.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "NBC_DIAG_2015")
public class NbcDiag2015 implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @Column(name = "OP_CREATE")
    private Integer opCreate;

    @Column(name = "UP_N")
    private Integer upN;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "SHORTS")
    private String shorts;

    @Column(name = "SHORTS_RU")
    private String shortsRu;

    @Column(name = "ORD")
    private Integer ord;

    @Column(name = "USEFUL")
    private Integer useful;

    @Column(name = "DESCRIPTION")
    private String description;

    public NbcDiag2015() {
    }

    public NbcDiag2015(Integer n) {
        this.n = n;
    }

    public NbcDiag2015(Integer n, Integer opCreate) {
        this.n = n;
        this.opCreate = opCreate;
    }

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

    public Integer getUpN() {
        return upN;
    }

    public void setUpN(Integer upN) {
        this.upN = upN;
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

    public String getShorts() {
        return shorts;
    }

    public void setShorts(String shorts) {
        this.shorts = shorts;
    }

    public String getShortsRu() {
        return shortsRu;
    }

    public void setShortsRu(String shortsRu) {
        this.shortsRu = shortsRu;
    }

    public Integer getOrd() {
        return ord;
    }

    public void setOrd(Integer ord) {
        this.ord = ord;
    }

    public Integer getUseful() {
        return useful;
    }

    public void setUseful(Integer useful) {
        this.useful = useful;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

