package lgk.nsbc.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Table(name = "NBC_PROC_EXECUTOR")
public class NbcProcExecutor implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @OneToOne
    @JoinColumn(name = "NBC_PROC_N")
    private NbcProc nbcProc;

    @OneToOne
    @JoinColumn(name = "NBC_EXECUTOR_N")
    private NbcExecutor nbcExecutor;

}