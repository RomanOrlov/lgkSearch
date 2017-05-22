package lgk.nsbc.spect.view.statistic;

import lgk.nsbc.model.Patients;
import lgk.nsbc.model.Proc;
import lgk.nsbc.model.SamplePatients;
import lgk.nsbc.util.DateUtils;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SampleBind {
    private Proc surgeryProc;
    private Patients patients;
    private SamplePatients samplePatients;
    private Integer ageAtSurgery;

    public String getInclusionRepsresentation() {
        if (samplePatients == null || samplePatients.getInclusion() == null)
            return "Неизвестно";
        if ("Y".equals(samplePatients.getInclusion()))
            return "Да";
        else if ("N".equals(samplePatients.getInclusion()))
            return "Нет";
        return "Неизвестно";
    }

    public Integer getAgeAtSurgery() {
        if (surgeryProc == null) return null;
        if (ageAtSurgery != null) return ageAtSurgery;
        LocalDate birthday = DateUtils.asLocalDate(patients.getPeople().getBirthday());
        LocalDate surgeryDate = DateUtils.asLocalDate(surgeryProc.getProcBeginTime());
        ageAtSurgery = Period.between(birthday, surgeryDate).getYears();
        return ageAtSurgery;
    }
}
