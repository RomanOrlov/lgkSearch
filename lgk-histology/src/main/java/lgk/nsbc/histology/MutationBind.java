package lgk.nsbc.histology;

import lgk.nsbc.model.dictionary.DicYesNo;
import lgk.nsbc.model.dictionary.Genes;
import lgk.nsbc.model.dictionary.MutationTypes;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MutationBind {
    private Genes genes;
    private MutationTypes mutationTypes;
    private DicYesNo dicYesNo;
}
