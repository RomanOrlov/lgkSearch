package lgk.nsbc.backend.entity;

import lgk.nsbc.backend.entity.dictionary.NbcDicBodyPart;
import lgk.nsbc.backend.entity.dictionary.NbcDicDynamic;
import lgk.nsbc.backend.entity.dictionary.NbcDicLocsd;
import lgk.nsbc.backend.entity.dictionary.NbcSymptomatologyGrade;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "NBC_SENSITIVE_DISTURB_1")
public class NbcSensitiveDisturb implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_STUD_N")
    private NbcStud nbcStud;

    @OneToOne
    @JoinColumn(name = "NBC_DIC_LOCSD_N")
    private NbcDicLocsd nbcDicLocsd;

    @OneToOne
    @JoinColumn(name = "NBC_DIC_BODY_PART_N")
    private NbcDicBodyPart nbcDicBodyPart;

    @OneToOne
    @JoinColumn(name = "GRADE")
    private NbcSymptomatologyGrade nbcSymptomatologyGrade;

    @OneToOne
    @JoinColumn(name = "DYNAMIC")
    private NbcDicDynamic nbcDicDynamic;

}

