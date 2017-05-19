package lgk.nsbc.backend.entity;

import lgk.nsbc.backend.entity.dvh.NbcDvh;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author Роман
 */
@Setter
@Getter
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

}