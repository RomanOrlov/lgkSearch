package lgk.nsbc.backend.entity.eye;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "NBC_VISUAL_FIELD_1")
public class NbcVisualField implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @Column(name = "DISORDER_PRESENCE")
    private Character disorderPresence;

    @Column(name = "HEMIANOPSIA")
    private Character hemianopsia;

    @Column(name = "CONCENTRIC_CONSTRICTION")
    private Character concentricConstriction;

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Character getDisorderPresence() {
        return disorderPresence;
    }

    public void setDisorderPresence(Character disorderPresence) {
        this.disorderPresence = disorderPresence;
    }

    public Character getHemianopsia() {
        return hemianopsia;
    }

    public void setHemianopsia(Character hemianopsia) {
        this.hemianopsia = hemianopsia;
    }

    public Character getConcentricConstriction() {
        return concentricConstriction;
    }

    public void setConcentricConstriction(Character concentricConstriction) {
        this.concentricConstriction = concentricConstriction;
    }
}