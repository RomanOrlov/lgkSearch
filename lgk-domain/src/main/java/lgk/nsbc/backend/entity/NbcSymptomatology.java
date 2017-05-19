package lgk.nsbc.backend.entity;

import lgk.nsbc.backend.entity.dictionary.NbcDicDynamic;
import lgk.nsbc.backend.entity.dictionary.NbcSymptomatologyGrade;
import lgk.nsbc.backend.entity.dictionary.NbcSymptomatologyName;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "NBC_SYMPTOMATOLOGY_1")
public class NbcSymptomatology implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne
    @JoinColumn(name = "NAME")
    private NbcSymptomatologyName nbcSymptomatologyName;

    @OneToOne
    @JoinColumn(name = "GRADE")
    private NbcSymptomatologyGrade nbcSymptomatologyGrade;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_STUD_N")
    private List<NbcStud> nbcStudies;

    @OneToOne
    @JoinColumn(name = "DYNAMIC")
    private NbcDicDynamic nbcDicDynamic;
}