package lgk.nsbc.histology;

import lgk.nsbc.model.Stud;
import lgk.nsbc.model.dictionary.StudType;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudBind {
    private Stud stud;
    private StudType studType;
}
