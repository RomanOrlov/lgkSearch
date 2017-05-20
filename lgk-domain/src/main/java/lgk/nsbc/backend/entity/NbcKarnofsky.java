package lgk.nsbc.backend.entity;

import lgk.nsbc.backend.entity.dictionary.NbcKarnofskyGrade;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "NBC_KARNOFSKY_1")
public class NbcKarnofsky implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_STUD_N")
    private NbcStud nbcStud;

    @OneToOne
    @JoinColumn(name = "GRADE")
    private NbcKarnofskyGrade nbcKarnofskyGrade;

}