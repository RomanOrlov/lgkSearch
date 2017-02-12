package lgk.nsbc.backend.entity.dvh;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Гистограммы доза-объём
 */
@Setter
@Getter
@Entity
@Table(name = "NBC_DVH")
public class NbcDvh implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToMany(mappedBy = "nbcDvh")
    private List<NbcDvhLines> nbcDvhLines;

    @OneToOne
    @JoinColumn(name = "DATA_VERSION")
    private NbcDvhDataVersion nbcDvhDataVersion;

    @OneToOne
    @JoinColumn(name = "LGP_VERSION")
    private NbcDvhLgpVersion nbcDvhLgpVersion;

    @OneToOne
    @JoinColumn(name = "DOSE_ALGORITHM")
    private NbcDvhDoseAlgorithm nbcDvhDoseAlgorithm;

    @Column(name = "DOSEMAX")
    private Double dosemax;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DOSEMIN")
    private Double dosemin;

    @Column(name = "N_BINS")
    private Integer nBins; // Число ячеек в гистограмме

    @Column(name = "BIN_SIZE")
    private Double binSize; // Размер ячейки в Гр

    public Integer getNBins() {
        return nBins;
    }

    public void setNBins(Integer nBins) {
        this.nBins = nBins;
    }

}