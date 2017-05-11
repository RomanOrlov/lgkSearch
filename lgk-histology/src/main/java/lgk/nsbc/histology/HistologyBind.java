package lgk.nsbc.histology;

import lgk.nsbc.model.Stud;
import lgk.nsbc.model.histology.Histology;
import lgk.nsbc.model.histology.Mutation;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Основная информация по гистологии
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistologyBind {
    private Stud stud;
    private Histology histology;
    private List<Mutation> mutations;
    private List<MutationBind> mutationBinds;

    private LocalDate histologyDate;
    private Boolean burdenkoVerification;
    private Double ki67From;
    private Double ki67To;
    private String comment;
}
