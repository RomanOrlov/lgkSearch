package lgk.nsbc.template.model.sys;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysAgents {
    private Long n;
    private Long op_create;
    private String name;
    private String pid;
    private Long people_n;
    private Long nbc_org_n_default;
}
