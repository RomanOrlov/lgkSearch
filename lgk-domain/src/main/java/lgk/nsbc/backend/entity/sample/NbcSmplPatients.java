package lgk.nsbc.backend.entity.sample;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigInteger;

@Setter
@Getter
@Entity
@Table(name = "NBC_SMPL_PATIENTS")
public class NbcSmplPatients implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "N")
    private Long n;

    @Column(name = "BAS_SAMPLES_N")
    private BigInteger basSamplesN;

    @Column(name = "NBC_PATIENTS_N")
    private BigInteger nbcPatientsN;

    @Column(name = "COMMENTS")
    private String comments;

    @Column(name = "INCLUSION")
    private Character inclusion;

}