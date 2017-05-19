package lgk.nsbc.histology;

import lgk.nsbc.model.dictionary.DicYesNo;
import lgk.nsbc.model.dictionary.Gene;
import lgk.nsbc.model.dictionary.MutationType;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MutationBind {
    private Gene gene;
    private MutationType mutationType;
    private DicYesNo dicYesNo;
}
