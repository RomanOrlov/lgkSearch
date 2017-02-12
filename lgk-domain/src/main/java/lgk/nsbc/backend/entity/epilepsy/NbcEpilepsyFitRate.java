package lgk.nsbc.backend.entity.epilepsy;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Оценка частоты приступов
 */
@Setter
@Getter
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

}