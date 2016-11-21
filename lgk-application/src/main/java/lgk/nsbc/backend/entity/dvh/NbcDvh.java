package lgk.nsbc.backend.entity.dvh;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Гистограммы доза-объём
 */
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

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Double getDosemax() {
        return dosemax;
    }

    public void setDosemax(Double dosemax) {
        this.dosemax = dosemax;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDosemin() {
        return dosemin;
    }

    public void setDosemin(Double dosemin) {
        this.dosemin = dosemin;
    }

    public Integer getNBins() {
        return nBins;
    }

    public void setNBins(Integer nBins) {
        this.nBins = nBins;
    }

    public Double getBinSize() {
        return binSize;
    }

    public void setBinSize(Double binSize) {
        this.binSize = binSize;
    }

    public List<NbcDvhLines> getNbcDvhLines() {
        return nbcDvhLines;
    }

    public void setNbcDvhLines(List<NbcDvhLines> nbcDvhLines) {
        this.nbcDvhLines = nbcDvhLines;
    }

    public NbcDvhDoseAlgorithm getNbcDvhDoseAlgorithm() {
        return nbcDvhDoseAlgorithm;
    }

    public void setNbcDvhDoseAlgorithm(NbcDvhDoseAlgorithm nbcDvhDoseAlgorithm) {
        this.nbcDvhDoseAlgorithm = nbcDvhDoseAlgorithm;
    }

    public NbcDvhLgpVersion getNbcDvhLgpVersion() {
        return nbcDvhLgpVersion;
    }

    public void setNbcDvhLgpVersion(NbcDvhLgpVersion nbcDvhLgpVersion) {
        this.nbcDvhLgpVersion = nbcDvhLgpVersion;
    }

    public NbcDvhDataVersion getNbcDvhDataVersion() {
        return nbcDvhDataVersion;
    }

    public void setNbcDvhDataVersion(NbcDvhDataVersion nbcDvhDataVersion) {
        this.nbcDvhDataVersion = nbcDvhDataVersion;
    }
}