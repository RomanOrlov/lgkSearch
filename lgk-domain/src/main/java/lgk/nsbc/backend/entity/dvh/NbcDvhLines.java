package lgk.nsbc.backend.entity.dvh;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "NBC_DVH_LINES")
public class NbcDvhLines implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "N")
    private Integer n;

    @ManyToOne
    @JoinColumn(name = "NBC_DVH_N")
    private NbcDvh nbcDvh;

    @Column(name = "DOSE")
    private Double dose; // Доза в Гр (центр ячейки)

    @Column(name = "VOLUME")
    private Double volume; // в мм^3

}