package lgk.nsbc.backend.entity.toxicity;

import lgk.nsbc.backend.entity.NbcPatients;
import lgk.nsbc.backend.entity.NbcStud;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "NBC_RTOG_TOXICITY")
public class NbcRtogToxicity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_STUD_N")
    private NbcStud nbcStud;

    @Column(name = "AMOUNT")
    private Integer amount;

    @OneToOne
    @JoinColumn(name = "GRADE")
    private NbcRtogToxicityGrade nbcRtogToxicityGrade;

    @OneToOne
    @JoinColumn(name = "PERIOD")
    private NbcRtogToxicityPeriod nbcRtogToxicityPeriod;

    @Column(name = "COMMENTARY")
    private String commentary;

    @Column(name = "BEGIN_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date beginDate;

    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @OneToOne
    @JoinColumn(name = "NBC_PATIENTS_N")
    private NbcPatients nbcPatients;

}
