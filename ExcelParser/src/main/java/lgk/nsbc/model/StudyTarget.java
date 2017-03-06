package lgk.nsbc.model;

import lombok.Data;

import java.util.List;

/**
 * Для хороидального сплетения и гипофиза это просто объем и отсчеты.
 * При этом,
 */
@Data
public class StudyTarget {
    private String diagnosis;
    private List<Target> targets;
}
