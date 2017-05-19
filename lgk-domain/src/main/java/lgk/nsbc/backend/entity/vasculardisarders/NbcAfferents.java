package lgk.nsbc.backend.entity.vasculardisarders;

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
@Table(name = "NBC_AFFERENTS_1")
public class NbcAfferents implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @Column(name = "ACA")
    private Character aca;

    @Column(name = "MCA")
    private Character mca;

    @Column(name = "PCA")
    private Character pca;

    @Column(name = "SCA")
    private Character sca;

    @Column(name = "AICA")
    private Character aica;

    @Column(name = "PICA")
    private Character pica;

    @Column(name = "VA")
    private Character va;

    @Column(name = "SCAA")
    private Character scaa;

    @Column(name = "ICAA")
    private Character icaa;

    @Column(name = "BA")
    private Character ba;

}