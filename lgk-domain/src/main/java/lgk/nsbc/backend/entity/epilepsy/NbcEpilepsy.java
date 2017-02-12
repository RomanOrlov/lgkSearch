package lgk.nsbc.backend.entity.epilepsy;

import lgk.nsbc.backend.entity.NbcStud;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "NBC_EPILEPSY_1")
public class NbcEpilepsy implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_STUD_N")
    private NbcStud nbcStud;

    @Column(name = "AMOUNT_FROM")
    private Integer amountFrom;

    @Column(name = "AMOUNT_TO")
    private Integer amountTo;

    @OneToOne
    @JoinColumn(name = "FIT_PERIOD")
    private NbcEpilepsyFitPeriod nbcEpilepsyFitPeriod;

    @OneToOne
    @JoinColumn(name = "FIT_RATE")
    private NbcEpilepsyFitRate nbcEpilepsyFitRate;

    @OneToOne
    @JoinColumn(name = "FIT_PROPERTY")
    private NbcEpilepsyFitProperty nbcEpilepsyFitProperty;

    @OneToOne
    @JoinColumn(name = "ENGEL")
    private NbcEpilepsyEngel nbcEpilepsyEngel;


}