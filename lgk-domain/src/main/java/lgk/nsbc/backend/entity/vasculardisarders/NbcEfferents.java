package lgk.nsbc.backend.entity.vasculardisarders;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "NBC_EFFERENTS_1")
public class NbcEfferents implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "N")
    private Integer n;

    @Column(name = "SUPERFICIAL")
    private Character superficial;

    @Column(name = "DEEP")
    private Character deep;

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Character getSuperficial() {
        return superficial;
    }

    public void setSuperficial(Character superficial) {
        this.superficial = superficial;
    }

    public Character getDeep() {
        return deep;
    }

    public void setDeep(Character deep) {
        this.deep = deep;
    }
}