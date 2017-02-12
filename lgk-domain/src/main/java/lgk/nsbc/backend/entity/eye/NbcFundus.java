package lgk.nsbc.backend.entity.eye;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Глазное дно
 */
@Setter
@Getter
@Entity
@Table(name = "NBC_FUNDUS_1")
public class NbcFundus implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "N")
    private Integer n;

    @Column(name = "DISORDER_PRESENCE")
    private Character disorderPresence;

    @Column(name = "CONGESTION")
    private Character congestion;

    @Column(name = "ATROPHY")
    private Character atrophy;

    @OneToOne
    @JoinColumn(name = "ATROPHY_GRADE")
    private NbcFundusAtrophyGrade nbcFundusAtrophyGrade;

}

