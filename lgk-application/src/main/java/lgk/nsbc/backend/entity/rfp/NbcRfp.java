package lgk.nsbc.backend.entity.rfp;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "NBC_RFP")
public class NbcRfp implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @Column(name = "NBC_FARMACY_N")
    private Integer nbcFarmacyN;

    @Column(name = "BAS_ISOTOPE_N")
    private Integer basIsotopeN;

    @Column(name = "ORD")
    private Integer ord;

    @Column(name = "USEFUL")
    private Integer useful;

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getNbcFarmacyN() {
        return nbcFarmacyN;
    }

    public void setNbcFarmacyN(Integer nbcFarmacyN) {
        this.nbcFarmacyN = nbcFarmacyN;
    }

    public Integer getBasIsotopeN() {
        return basIsotopeN;
    }

    public void setBasIsotopeN(Integer basIsotopeN) {
        this.basIsotopeN = basIsotopeN;
    }
}