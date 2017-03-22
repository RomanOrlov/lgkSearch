package lgk.nsbc.template.model.sys;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysSessions {
    private Long n;
    private String sid;
    private Long agent_n;
    private Date opened;
}
