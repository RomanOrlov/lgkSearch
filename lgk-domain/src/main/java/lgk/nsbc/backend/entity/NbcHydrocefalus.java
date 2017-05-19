package lgk.nsbc.backend.entity;

import lgk.nsbc.backend.entity.dictionary.NbcDicDynamic;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Гидроцефалия
 */
@Setter
@Getter
@Entity
@Table(name = "NBC_HYDROCEFALUS_1")
public class NbcHydrocefalus implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_STUD_N")
    private NbcStud nbcStud;

    @OneToOne
    @JoinColumn(name = "DYNAMIC")
    private NbcDicDynamic nbcDicDynamic;

    @Column(name = "LESSION_PRESENCE")
    private Character lessionPresence;

}