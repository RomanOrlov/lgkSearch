package lgk.nsbc.backend.entity;

import lgk.nsbc.backend.entity.dictionary.NbcExecutorRoles;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "NBC_EXECUTOR")
public class NbcExecutor implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne
    @JoinColumn(name = "NBC_STAFF_N")
    private NbcStaff nbcStaff;

    @OneToOne
    @JoinColumn(name = "ROLES")
    private NbcExecutorRoles nbcExecutorRoles;

    @Column(name = "CONFIRMATION")
    private Character confirmation;

}