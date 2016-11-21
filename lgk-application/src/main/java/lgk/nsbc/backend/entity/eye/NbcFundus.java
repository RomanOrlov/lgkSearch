package lgk.nsbc.backend.entity.eye;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Глазное дно
 */
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

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Character getDisorderPresence() {
        return disorderPresence;
    }

    public void setDisorderPresence(Character disorderPresence) {
        this.disorderPresence = disorderPresence;
    }

    public Character getCongestion() {
        return congestion;
    }

    public void setCongestion(Character congestion) {
        this.congestion = congestion;
    }

    public Character getAtrophy() {
        return atrophy;
    }

    public void setAtrophy(Character atrophy) {
        this.atrophy = atrophy;
    }

    public NbcFundusAtrophyGrade getNbcFundusAtrophyGrade() {
        return nbcFundusAtrophyGrade;
    }

    public void setNbcFundusAtrophyGrade(NbcFundusAtrophyGrade nbcFundusAtrophyGrade) {
        this.nbcFundusAtrophyGrade = nbcFundusAtrophyGrade;
    }
}

