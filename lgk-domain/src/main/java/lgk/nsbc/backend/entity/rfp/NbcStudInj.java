package lgk.nsbc.backend.entity.rfp;

import lgk.nsbc.backend.entity.NbcStud;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Параметры вводимых препаратов (контраст для КТ, МРТ) РФП для ПЭТ (или ОФЕКТ)
 */
@Setter
@Getter
@Entity
@Table(name = "NBC_STUD_INJ")
public class NbcStudInj implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_STUD_N")
    private NbcStud nbcStud;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_RFP_N")
    private NbcRfp nbcRfp;

    @Column(name = "INJ_ACTIVITY_BQ")
    private Double injActivityBq;

    @Column(name = "INJ_VOL_ML")
    private Double injVolMl;

    @Column(name = "INJ_BEGIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date injBegin;

    @Column(name = "INJ_END")
    @Temporal(TemporalType.TIMESTAMP)
    private Date injEnd;

}