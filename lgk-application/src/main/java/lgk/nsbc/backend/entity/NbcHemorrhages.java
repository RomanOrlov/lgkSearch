package lgk.nsbc.backend.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "NBC_HEMORRHAGES_1")
public class NbcHemorrhages implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_STUD_N")
    private NbcStud nbcStud;

    @Column(name = "AMOUNT")
    private Integer amount; // Количество эпизодов

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public NbcStud getNbcStud() {
        return nbcStud;
    }

    public void setNbcStud(NbcStud nbcStud) {
        this.nbcStud = nbcStud;
    }
}