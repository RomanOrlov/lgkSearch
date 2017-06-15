package lgk.nsbc.spect.view.statistic;

import lgk.nsbc.model.*;
import lgk.nsbc.model.dao.dictionary.ProcTimeApproxDao;
import lgk.nsbc.model.dictionary.DicYesNo;
import lgk.nsbc.spect.util.DateUtils;
import lgk.nsbc.spect.view.spectcrud.SpectGridData;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.YEARS;
import static lgk.nsbc.spect.view.statistic.Causes.*;
import static lgk.nsbc.spect.view.statistic.Censor.CENSORED;
import static lgk.nsbc.spect.view.statistic.Censor.NOT_CENSORED;
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
    private LocalDate surgeryProcDate;
    private Proc radioProc;
    private Patients patients;
    private SamplePatients samplePatients;
    private SpectGridData spect1;
    private SpectGridData spect2;
    private SpectGridData spect3;
    private DicYesNo idh1;
    private DicYesNo idh2;
    private DicYesNo mgmt;
    private Long ageAtSurgery;

    private List<Stud> studList;
    private List<Proc> procList;
    private Map<Stud, List<FollowUp>> mriFollowUps;

    private Long survival;
    private String survivalCause;
    private Censor survivalCensor;

    private LocalDate lastDate;
    private String isReccurance;

    private Long recurrence;
    private String recurrenceCause;
    private Censor recurrenceCensor;

    public LocalDate getSpect1Date() {
        return spect1 == null ? null : spect1.getStudyDate();
    }

    public Double getSpect1InEarly() {
        return spect1 == null ? null : spect1.getInSphereEarly();
    }

    public Double getSpect1InLate() {
        return spect1 == null ? null : spect1.getInSphereLate();
    }

    public Double getSpect1InOut() {
        return spect1 == null ? null : spect1.getInSphereOut();
    }

    public LocalDate getSpect2Date() {
        return spect2 == null ? null : spect2.getStudyDate();
    }

    public Double getSpect2InEarly() {
        return spect2 == null ? null : spect2.getInSphereEarly();
    }

    public Double getSpect2InLate() {
        return spect2 == null ? null : spect2.getInSphereLate();
    }

    public Double getSpect2InOut() {
        return spect2 == null ? null : spect2.getInSphereOut();
    }

    public LocalDate getSpect3Date() {
        return spect3 == null ? null : spect3.getStudyDate();
    }

    public Double getSpect3InEarly() {
        return spect3 == null ? null : spect3.getInSphereEarly();
    }

    public Double getSpect3InLate() {
        return spect3 == null ? null : spect3.getInSphereLate();
    }

    public Double getSpect3InOut() {
        return spect3 == null ? null : spect3.getInSphereOut();
    }

    public Double getSpect1In30Isoline10() {
        return spect1 == null ? null : spect1.getInIsoline10Early();
    }

    public Double getSpect1In60Isoline10() {
        return spect1 == null ? null : spect1.getInIsoline10Late();
    }

    public Double getSpect1In30Isoline25() {
        return spect1 == null ? null : spect1.getInIsoline25Early();
    }

    public Double getSpect1In60Isoline25() {
        return spect1 == null ? null : spect1.getInIsoline25Late();
    }

    public Double getSpect1In30Isoline50() {
        return spect1 == null ? null : spect1.getInIsoline50Early();
    }

    public Double getSpect1In60Isoline50() {
        return spect1 == null ? null : spect1.getInIsoline50Late();
    }

    public Double getSpect2In30Isoline10() {
        return spect2 == null ? null : spect2.getInIsoline10Early();
    }

    public Double getSpect2In60Isoline10() {
        return spect2 == null ? null : spect2.getInIsoline10Late();
    }

    public Double getSpect2In30Isoline25() {
        return spect2 == null ? null : spect2.getInIsoline25Early();
    }

    public Double getSpect2In60Isoline25() {
        return spect2 == null ? null : spect2.getInIsoline25Late();
    }

    public Double getSpect2In30Isoline50() {
        return spect2 == null ? null : spect2.getInIsoline50Early();
    }

    public Double getSpect2In60Isoline50() {
        return spect2 == null ? null : spect2.getInIsoline50Late();
    }

    public LocalDate getSurgeryDate() {
        if (surgeryProcDate != null) return surgeryProcDate;
        if (surgeryProc != null && surgeryProc.getProcBeginTime() != null) {
            surgeryProcDate = DateUtils.asLocalDate(surgeryProc.getProcBeginTime());
        }
        return surgeryProcDate;
    }

    public LocalDate getRtBeginDate() {
        if (radioProc != null && radioProc.getProcBeginTime() != null) {
            return DateUtils.asLocalDate(radioProc.getProcBeginTime());
        }
        return null;
    }

    public LocalDate getRtEndDate() {
        if (radioProc != null && radioProc.getProcEndTime() != null) {
            return DateUtils.asLocalDate(radioProc.getProcEndTime());
        }
        return null;
    }

    public Long getSurgeryRTPeriod() {
        LocalDate surgeryDate = getSurgeryDate();
        LocalDate rtDate = getRtBeginDate();
        if (surgeryDate != null && rtDate != null) {
            return DAYS.between(surgeryDate, rtDate);
        }
        return null;
    }

    public String getInclusionRepresentation() {
        if (Objects.equals("Y", samplePatients.getInclusion()))
            return "Да";
        else if ("N".equals(samplePatients.getInclusion()))
            return "Нет";
        return "Неизвестно";
    }

    public void setInclusionRepresentation(String s) {
        if (Objects.equals("Да", s))
            samplePatients.setInclusion("Y");
        else if (Objects.equals("Нет", s))
            samplePatients.setInclusion("N");
        else
            samplePatients.setInclusion(null);
    }

    public Long getAgeAtSurgery() {
        if (ageAtSurgery != null) return ageAtSurgery;
        LocalDate birthday = DateUtils.asLocalDate(patients.getPeople().getBirthday());
        LocalDate secondDate = getClosestDate();
        ageAtSurgery = YEARS.between(birthday, secondDate);
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

    public void recalculateSurvivalAndRecurrence() {
        if (surgeryProc == null) {
            survival = null;
            survivalCause = null;
            survivalCensor = null;
            recurrence = null;
            recurrenceCause = null;
            recurrenceCensor = null;
            return;
        }
        LocalDate obit = patients.getPeople().getObit();
        LocalDate surgeryDate = DateUtils.asLocalDate(surgeryProc.getProcBeginTime());
        if (obit != null) {
            long between = DAYS.between(surgeryDate, obit);
            survival = between;
            survivalCause = DEATH.toString();
            survivalCensor = NOT_CENSORED;
            recurrence = between;
            recurrenceCause = DEATH.toString();
            recurrenceCensor = NOT_CENSORED;
            return;
        }
        // Считаем с рассчетом того, что надо смотреть в процедупы.
        recalculateSurvival();
        recalculateRecurrence();
    }

    private void recalculateSurvival() {
        Map<String, Date> studs = studList.stream()
                .filter(Objects::nonNull)
                .filter(stud -> stud.getStudyDateTime() != null)
                .filter(stud -> stud.getStudType() != null)
                .collect(Collectors.toMap(
                        stud -> LAST_HAPPENED_STUDY + " типа " + stud.getStudType().toString() + " от " + DateUtils.asLocalDate(stud.getStudyDateTime()),
                        Stud::getStudyDateTime,
                        (date, date2) -> date
                ));

        Map<String, Date> procedures = procList.stream()
                .filter(Objects::nonNull)
                .filter(proc -> proc.getProcBeginTime() != null)
                .filter(proc -> proc.getProcTimeApprox() != null)
                .filter(proc -> Objects.equals(proc.getProcTimeApprox().getN(), ProcTimeApproxDao.HAPPENED))
                .collect(Collectors.toMap(
                        proc -> LAST_HAPPENED_PROCUDERE + " типа " + proc.getProcType().toString() + " от " + DateUtils.asLocalDate(proc.getProcBeginTime()),
                        Proc::getProcBeginTime
                ));

        Map<String, Date> dateMap = new HashMap<>();
        dateMap.putAll(studs);
        dateMap.putAll(procedures);
        Optional<Map.Entry<String, Date>> max = dateMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue().after(surgeryProc.getProcBeginTime()))
                .max(Comparator.comparing(Map.Entry::getValue));
        if (max.isPresent()) {
            Map.Entry<String, Date> entry = max.get();
            LocalDate last = DateUtils.asLocalDate(entry.getValue());
            LocalDate surgeryDate = DateUtils.asLocalDate(surgeryProc.getProcBeginTime());
            survival = DAYS.between(surgeryDate, last);
            survivalCause = entry.getKey();
            survivalCensor = CENSORED;
        } else {
            survival = null;
            survivalCause = null;
            survivalCensor = null;
        }
    }

    /**
     * 1. Ищем все мрт исследования
     * 2. Ищем все мишени и контроли по этим исследованиям
     * 3.
     */
    private void recalculateRecurrence() {
        /*studList.stream()
                .filter(Objects::nonNull)
                .filter(stud -> stud.getStudyDateTime() != null)
                .filter(stud -> Objects.equals(stud.getStudType().getN(), StudTypeDao.MRI_TYPE));*/
        LocalDate surgeryDate = getSurgeryDate();
        if (surgeryDate != null && lastDate != null) {
            recurrence = DAYS.between(surgeryDate, lastDate);
            recurrenceCensor = isReccurance.equals("y") ? NOT_CENSORED : CENSORED;
        }
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
