package lgk.nsbc.template.model.sys;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysOperations {
    private Long n;
    private Long session_n;
    private String command_name;
    private Date moment;
}
