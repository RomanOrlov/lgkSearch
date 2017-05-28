package lgk.nsbc.histology;

import lgk.nsbc.model.Stud;
import lgk.nsbc.model.dao.dictionary.GenesDao;
import lgk.nsbc.model.dictionary.Gene;
import lgk.nsbc.model.histology.Histology;
import lgk.nsbc.model.histology.Mutation;
import lombok.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

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
    private Set<Mutation> mutations;

    private LocalDate histologyDate;
    private Boolean burdenkoVerification;
    private Double ki67From;
    private Double ki67To;
    private String comment;

    public static Boolean getBooleanBurdenkoVerification(String burdVeruf) {
        return Objects.equals(burdVeruf, "Y");
    }

    public static String getStringBurdenkoVerification(Boolean burdenkoVerification) {
        if (burdenkoVerification == null) return null;
        return burdenkoVerification ? "Y" : "N";
    }

    public static Set<Mutation> getMutationsTemplate() {
        Map<Long, Gene> genes = GenesDao.getGenes();
        return genes.values()
                .stream()
                .map(gene -> Mutation.builder()
                        .gene(gene)
                        .build())
                .sorted()
                .collect(Collectors.toCollection(TreeSet::new));
    }
}
