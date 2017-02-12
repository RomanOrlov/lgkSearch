package lgk.nsbc.backend.entity.eye;

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

}