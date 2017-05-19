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

}