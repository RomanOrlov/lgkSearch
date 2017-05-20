package lgk.nsbc.backend.entity.rfp;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Setter
@Getter
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

}