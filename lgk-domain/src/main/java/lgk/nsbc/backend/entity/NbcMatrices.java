package lgk.nsbc.backend.entity;

import lgk.nsbc.backend.entity.dvh.NbcDvh;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author Роман
 */
@Entity
@Table(name = "NBC_MATRICES")
public class NbcMatrices implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_DVH_N")
    private List<NbcDvh> nbcDvh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_PROC_N")
    private NbcProc nbcProc;

    @Column(name = "NAME")
    private String name;

    @Column(name = "GRIDSIZE")
    private Double gridsize;

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getGridsize() {
        return gridsize;
    }

    public void setGridsize(Double gridsize) {
        this.gridsize = gridsize;
    }

    public NbcProc getNbcProc() {
        return nbcProc;
    }

    public void setNbcProc(NbcProc nbcProc) {
        this.nbcProc = nbcProc;
    }

    public List<NbcDvh> getNbcDvh() {
        return nbcDvh;
    }

    public void setNbcDvh(List<NbcDvh> nbcDvh) {
        this.nbcDvh = nbcDvh;
    }
}