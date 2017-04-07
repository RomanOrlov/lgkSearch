package lgk.nsbc.view.spectflup.bind;

import com.vaadin.spring.annotation.SpringComponent;
import lgk.nsbc.dao.*;
import lgk.nsbc.model.*;
import lgk.nsbc.model.spect.ContourType;
import lgk.nsbc.model.spect.TargetType;
import lgk.nsbc.util.DateUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static lgk.nsbc.model.spect.ContourType.SPHERE;
import static lgk.nsbc.model.spect.MainInfo.*;
import static lgk.nsbc.model.spect.TargetType.*;
import static lgk.nsbc.util.DateUtils.asDate;

/**
 * Полное представление того, как хранятся данные в базе
 */
@SpringComponent
@Scope("prototype")
@NoArgsConstructor
@Getter
@Setter
public class SpectDBData {
    @Autowired
    private NbcStudInjDao nbcStudInjDao;
    @Autowired
    private NbcStudDao nbcStudDao;
    @Autowired
    private NbcFollowUpDao nbcFollowUpDao;
    @Autowired
    private NbcFlupSpectDataDao nbcFlupSpectDataDao;
    @Autowired
    private NbcTargetDao nbcTargetDao;
    @Autowired
    private DSLContext context;

    private Date studyDate;
    private NbcPatients nbcPatients;
    private NbcStud nbcStud;
    private NbcStudInj nbcStudInj;
    private Map<NbcFollowUp, List<NbcFlupSpectData>> spectData = new HashMap<>();
    private Map<NbcFollowUp, NbcTarget> followTargetMap = new HashMap<>();

    /**
     * Редактирование
     *
     * @param nbcPatients
     * @param studyDate
     */
    public SpectDBData(NbcPatients nbcPatients, Date studyDate) {
        this.nbcPatients = nbcPatients;
        this.studyDate = studyDate;
    }

    /**
     * Создание новой
     *
     * @param nbcPatients
     */
    public SpectDBData(NbcPatients nbcPatients) {
        this.nbcPatients = nbcPatients;
    }

    @PostConstruct
    public void init() {
        if (studyDate == null) return;
        // Вытаскиваем данные
        Optional<NbcStud> nbcStudOptional = nbcStudDao.findSpectStudyByDate(nbcPatients, studyDate);
        if (!nbcStudOptional.isPresent()) return;
        nbcStud = nbcStudOptional.get();
        Optional<NbcStudInj> studInjOptional = nbcStudInjDao.findByStudy(nbcStud);
        studInjOptional.ifPresent(optional -> nbcStudInj = optional);
        List<NbcFollowUp> nbcFollowUps = nbcFollowUpDao.findByStudy(nbcStud);
        for (NbcFollowUp nbcFollowUp : nbcFollowUps) {
            NbcTarget nbcTarget = nbcTargetDao.findTargetById(nbcFollowUp.getNbc_target_n());
            followTargetMap.put(nbcFollowUp, nbcTarget);
            List<NbcFlupSpectData> byNbcFollowup = nbcFlupSpectDataDao.findByNbcFollowup(nbcFollowUp);
            spectData.put(nbcFollowUp, byNbcFollowup);
        }
    }

    /**
     * @param spectFlupData
     */
    public void readFromDB(SpectFlupData spectFlupData) {
        if (nbcStudInj != null)
            spectFlupData.setDose(nbcStudInj.getInj_activity_bq());
        if (nbcStud != null)
            spectFlupData.setStudyDate(DateUtils.asLocalDate(nbcStud.getStudydatetime()));
        int targetsCount = 0;
        for (Map.Entry<NbcFollowUp, List<NbcFlupSpectData>> entry : spectData.entrySet()) {
            NbcFollowUp nbcFollowUp = entry.getKey();
            NbcTarget nbcTarget = followTargetMap.get(nbcFollowUp);
            String targetProperty = String.valueOf(targetsCount) + TARGET.toString();
            spectFlupData.setValueByPropertyName(targetProperty, nbcTarget);
            List<NbcFlupSpectData> dataList = spectData.get(nbcFollowUp);
            for (NbcFlupSpectData nbcFlupSpectData : dataList) {
                String propertyPrefix = getPropertyPrefix(nbcFlupSpectData.getTargetType(), nbcFlupSpectData.getContourType(), targetsCount);
                spectFlupData.setValueByPropertyName(propertyPrefix + VOLUME.toString(), nbcFlupSpectData.getVolume());
                spectFlupData.setValueByPropertyName(propertyPrefix + MIN30.toString(), nbcFlupSpectData.getEarly_phase());
                spectFlupData.setValueByPropertyName(propertyPrefix + MIN60.toString(), nbcFlupSpectData.getLate_phase());
            }
            targetsCount++;
        }
    }

    private String getPropertyPrefix(TargetType targetType, ContourType contourType, Integer targetCount) {
        if (targetType == TARGET)
            return String.valueOf(targetCount) + targetType.toString() + contourType.toString();
        else
            return targetType.toString() + contourType.toString();
    }

    public void writeToDB(SpectFlupData data) {
        if (nbcStud == null) {
            nbcStud = NbcStud.builder().nbc_patients_n(nbcPatients.getN())
                    .study_type(11L)
                    .studydatetime(asDate(data.getStudyDate()))
                    .build();
            nbcStudDao.createNbcStud(nbcStud);
        } else if (nbcStud.getStudydatetime().getTime() != asDate(data.getStudyDate()).getTime()) {
            nbcStud.setStudydatetime(asDate(data.getStudyDate()));
            nbcStudDao.updateStudy(nbcStud);
        }
        if (nbcStudInj == null) {
            nbcStudInj = NbcStudInj.builder()
                    .nbc_stud_n(nbcStud.getN())
                    .inj_activity_bq(data.getDose())
                    .build();
            nbcStudInjDao.insertStudInj(nbcStudInj);
        } else if (nbcStud.getStudydatetime().getTime() != asDate(data.getStudyDate()).getTime()) {
            nbcStudDao.updateStudy(nbcStud);
        }
        // Удаляем старое
        List<NbcFlupSpectData> oldAllSpectFlupData = spectData.values()
                .stream()
                .flatMap(List::stream)
                .collect(toList());
        nbcFlupSpectDataDao.deleteSpectData(oldAllSpectFlupData);
        nbcFollowUpDao.deleteFollowUp(spectData.keySet());

        Map<String, NbcTarget> selectedTargets = data.getSelectedTargets();
        Map<String, Double> volumeAndPhases = data.getVolumeAndPhases();
        int targetNum = 0;
        for (Map.Entry<String, NbcTarget> entry : selectedTargets.entrySet()) {
            if (entry.getValue() == null) continue;
            NbcFollowUp nbcFollowUp = NbcFollowUp.builder()
                    .nbc_stud_n(nbcStud.getN())
                    .nbc_target_n(entry.getValue().getN())
                    .build();
            List<NbcFlupSpectData> dataList = new ArrayList<>();
            for (ContourType contourType : ContourType.values()) {
                String propertyName = String.valueOf(targetNum) + TARGET.toString() + contourType.toString();
                NbcFlupSpectData build = NbcFlupSpectData.builder()
                        .volume(volumeAndPhases.get(propertyName + VOLUME.toString()))
                        .early_phase(volumeAndPhases.get(propertyName + MIN30.toString()))
                        .late_phase(volumeAndPhases.get(propertyName + MIN60.toString()))
                        .targetType(TARGET)
                        .contourType(contourType)
                        .build();
                dataList.add(build);
            }
            dataList.add(getHyp(data));
            dataList.addAll(getHiz(data));
            nbcFlupSpectDataDao.createSpectFollowUpData(nbcFollowUp, dataList);
            targetNum++;
        }

    }

    private NbcFlupSpectData getHyp(SpectFlupData spectFlupData) {
        Map<String, Double> volumeAndPhases = spectFlupData.getVolumeAndPhases();
        return NbcFlupSpectData.builder()
                .volume(volumeAndPhases.get(HYP.toString() + SPHERE.toString() + VOLUME.toString()))
                .early_phase(volumeAndPhases.get(HYP.toString() + SPHERE.toString() + MIN30.toString()))
                .late_phase(volumeAndPhases.get(HYP.toString() + SPHERE.toString() + MIN60.toString()))
                .targetType(HYP)
                .contourType(SPHERE)
                .build();
    }

    private List<NbcFlupSpectData> getHiz(SpectFlupData spectFlupData) {
        Map<String, Double> volumeAndPhases = spectFlupData.getVolumeAndPhases();
        List<NbcFlupSpectData> dataList = new ArrayList<>();
        for (ContourType contourType : ContourType.values()) {
            NbcFlupSpectData spectData = NbcFlupSpectData.builder()
                    .volume(volumeAndPhases.get(HIZ.toString() + contourType.toString() + VOLUME.toString()))
                    .early_phase(volumeAndPhases.get(HIZ.toString() + contourType.toString() + MIN30.toString()))
                    .late_phase(volumeAndPhases.get(HIZ.toString() + contourType.toString() + MIN60.toString()))
                    .targetType(HIZ)
                    .contourType(contourType)
                    .build();
            dataList.add(spectData);
        }
        return dataList;
    }
}
