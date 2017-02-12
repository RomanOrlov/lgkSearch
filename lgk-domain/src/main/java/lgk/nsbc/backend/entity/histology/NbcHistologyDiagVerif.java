package lgk.nsbc.backend.entity.histology;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "NBC_HISTOLOGY_1_DIAG_VERIF")
public class NbcHistologyDiagVerif implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TEXT")
    private String text;

}