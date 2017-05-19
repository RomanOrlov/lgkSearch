package lgk.nsbc.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmplPatients implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private Long sample;
    private Long patient;
    private String comment;
    private String inclusion;
}
