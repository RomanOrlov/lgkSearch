package lgk.nsbc.spect.view.statistic;

import lgk.nsbc.model.Patients;
import lgk.nsbc.model.Proc;
import lgk.nsbc.model.SamplePatients;
import lgk.nsbc.model.dictionary.DicYesNo;
import lgk.nsbc.spect.view.spectcrud.SpectGridData;
import lgk.nsbc.util.DateUtils;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

import static lgk.nsbc.spect.view.statistic.Dynamic.NEGATIVE;
import static lgk.nsbc.spect.view.statistic.Dynamic.POSITIVE;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SampleBind implements Serializable, Comparable<SampleBind> {
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
    private Integer ageAtSurgery;

    private Causes causes;
    private Censor censor;
    private Dynamic dynamic;

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

    public Double getSpect2InEarly() {
        if (spect2 == null) return null;
        return spect2.getInEarly();
    }

    public Double getSpect2InLate() {
        if (spect2 == null) return null;
        return spect2.getInLate();
    }

    public Double getSpect2InOut() {
        if (spect2 == null) return null;
        return spect2.getInOut();
    }

    public Double getSpect3InEarly() {
        if (spect3 == null) return null;
        return spect3.getInEarly();
    }

    public Double getSpect3InLate() {
        if (spect3 == null) return null;
        return spect3.getInLate();
    }

    public Double getSpect3InOut() {
        if (spect3 == null) return null;
        return spect3.getInOut();
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

    public Double getRecurrencePeriod() {
        return null;
    }

    public Censor getRecurrenceTimeCensoredStatus() {
        return null;
    }

    public Causes getRecurrenceCause() {
        return null;
    }

    public Double getSurvivalPeriod() {
        return null;
    }

    public Censor getSurvivalPeriodCensoredStatus() {
        return null;
    }

    public Causes getSurvivalPeriodCause() {
        return null;
    }

    public Dynamic getSpect1_2Dynamic30() {
        Double spect1InEarly = getSpect1InEarly();
        Double spect2InEarly = getSpect2InEarly();
        if (spect1InEarly != null && spect2InEarly != null)
            return spect1InEarly < spect2InEarly ? POSITIVE : NEGATIVE;
        return null;
    }

    public Dynamic getSpect1_2Dynamic60() {
        Double spect1InLate = getSpect1InLate();
        Double spect2InLate = getSpect2InLate();
        if (spect1InLate != null && spect2InLate != null)
            return spect1InLate < spect2InLate ? POSITIVE : NEGATIVE;
        return null;
    }

    public Dynamic getSpect2_3Dynamic30() {
        Double spect2InEarly = getSpect2InEarly();
        Double spect3InEarly = getSpect3InEarly();
        if (spect2InEarly != null && spect3InEarly != null)
            return spect2InEarly < spect3InEarly ? POSITIVE : NEGATIVE;
        return null;
    }

    public Dynamic getSpect2_3Dynamic60() {
        Double spect2InLate = getSpect2InLate();
        Double spect3InLate = getSpect3InLate();
        if (spect2InLate != null && spect3InLate != null)
            return spect2InLate < spect3InLate ? POSITIVE : NEGATIVE;
        return null;
    }

    @Override
    public String toString() {
        return patients == null ? "" : patients.toStringWithCaseHistory();
    }

    @Override
    public int compareTo(SampleBind o) {
        return patients.getFullName().compareTo(o.getPatients().getFullName());
    }
}
