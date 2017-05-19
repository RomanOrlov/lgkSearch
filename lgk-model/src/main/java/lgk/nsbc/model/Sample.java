package lgk.nsbc.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sample implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private String code;
    private String name;
    private Long sysAgent;
    private String description;
    private String script;
    private String actuality;

}
