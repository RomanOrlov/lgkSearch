package lgk.nsbc.spect.view.statistic;

import lgk.nsbc.model.Patients;
import lgk.nsbc.model.Proc;
import lgk.nsbc.model.SamplePatients;
import lgk.nsbc.model.dictionary.DicYesNo;
import lgk.nsbc.model.histology.Histology;
import lgk.nsbc.spect.view.spectcrud.SpectGridData;
import lgk.nsbc.util.DateUtils;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SampleBind implements Serializable {
    private static final long serialVersionUID = 1L;

    private Proc surgeryProc;
    private Proc radioProc;
    private Patients patients;
    private SamplePatients samplePatients;
    private SpectGridData spect1;
    private SpectGridData spect2;
    private SpectGridData spect3;
    private DicYesNo idh1;
    private DicYesNo idh2;
    private DicYesNo mgmt;
    private Histology histology;
    private Integer ageAtSurgery;

    public Double getSpect1InEarly() {
        if (spect1 == null) return null;
        return spect1.getInEarly();
    }

    public Double getSpect1InLate() {
        if (spect1 == null) return null;
        return spect1.getInLate();
    }

    public Double getSpect1InOut() {
        if (spect1 == null) return null;
        return spect1.getInOut();
    }

    public String getInclusionRepresentation() {
        if (samplePatients == null || samplePatients.getInclusion() == null)
            return "Неизвестно";
        if ("Y".equals(samplePatients.getInclusion()))
            return "Да";
        else if ("N".equals(samplePatients.getInclusion()))
            return "Нет";
        return "Неизвестно";
    }

    public void setInclusionRepresentation(String s) {
        if (s == null || "Неизвестно".equals(s))
            samplePatients.setInclusion(null);
        else if ("Да".equals(s))
            samplePatients.setInclusion("Y");
        else if ("Нет".equals(s))
            samplePatients.setInclusion("N");
    }

    public Integer getAgeAtSurgery() {
        if (surgeryProc == null && radioProc == null && spect1 == null) return null;
        if (ageAtSurgery != null) return ageAtSurgery;
        LocalDate birthday = DateUtils.asLocalDate(patients.getPeople().getBirthday());
        LocalDate secondDate = getClosestDate();
        ageAtSurgery = Period.between(birthday, secondDate).getYears();
        return ageAtSurgery;
    }

    private LocalDate getClosestDate() {
        if (surgeryProc != null)
            return DateUtils.asLocalDate(surgeryProc.getProcBeginTime());
        else if (radioProc != null)
            return DateUtils.asLocalDate(radioProc.getProcBeginTime());
        else if (spect1 != null)
            return spect1.getStudyDate();
        return LocalDate.now();
    }
}
