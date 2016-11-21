package lgk.nsbc.backend.entity.dvh;

import javax.persistence.*;
import java.io.Serializable;

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

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Double getDose() {
        return dose;
    }

    public void setDose(Double dose) {
        this.dose = dose;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public NbcDvh getNbcDvh() {
        return nbcDvh;
    }

    public void setNbcDvh(NbcDvh nbcDvh) {
        this.nbcDvh = nbcDvh;
    }
}