package lgk.nsbc.backend.entity.epilepsy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Оценка частоты приступов
 */
@Entity
@Table(name = "NBC_EPILEPSY_1_FIT_RATE")
public class NbcEpilepsyFitRate implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Long n;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "AMOUNT")
    private Integer amount;

    @Column(name = "AMOUNT_FROM")
    private Integer amountFrom;

    @Column(name = "AMOUNT_TO")
    private Integer amountTo;
    @OneToOne
    @JoinColumn(name = "FIT_PERIOD")
    private NbcEpilepsyFitPeriod nbcEpilepsyFitPeriod;

    public Long getN() {
        return n;
    }

    public void setN(Long n) {
        this.n = n;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmountFrom() {
        return amountFrom;
    }

    public void setAmountFrom(Integer amountFrom) {
        this.amountFrom = amountFrom;
    }

    public Integer getAmountTo() {
        return amountTo;
    }

    public void setAmountTo(Integer amountTo) {
        this.amountTo = amountTo;
    }

    public NbcEpilepsyFitPeriod getNbcEpilepsyFitPeriod() {
        return nbcEpilepsyFitPeriod;
    }

    public void setNbcEpilepsyFitPeriod(NbcEpilepsyFitPeriod nbcEpilepsyFitPeriod) {
        this.nbcEpilepsyFitPeriod = nbcEpilepsyFitPeriod;
    }
}