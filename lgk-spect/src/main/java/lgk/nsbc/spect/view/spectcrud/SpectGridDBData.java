package lgk.nsbc.spect.view.spectcrud;

import lgk.nsbc.model.*;
import lgk.nsbc.util.DateUtils;
import lombok.Getter;

import java.util.*;

import static lgk.nsbc.model.spect.ContourType.*;
import static lgk.nsbc.model.spect.TargetType.*;

/**
 * Класс представляющий собой одну строчку данных.
 */
@Getter
public class SpectGridDBData {
    private final Patients patients;
    private final Stud stud;
    private final FollowUp followUp;
    private final StudInj studInj;
    private final Target target;
    private final List<FlupSpectData> dataList;
    private final Map<String, FlupSpectData> dataMap;
    private final Collection<Target> targets;

    public SpectGridDBData(Patients patients, Stud stud, FollowUp followUp, StudInj studInj, Target target, List<FlupSpectData> dataList, Collection<Target> targets) {
        this.patients = patients;
        this.stud = stud;
        this.followUp = followUp;
        this.studInj = studInj;
        this.target = target;
        this.dataList = dataList;
        this.targets = targets;
        dataMap = new HashMap<>();
        // Намного облегчает дальнейший парсинг данных
        dataList.forEach(nbcFlupSpectData -> {
            String property = nbcFlupSpectData.getTargetType().toString() + nbcFlupSpectData.getContourType().toString();
            dataMap.put(property, nbcFlupSpectData);
        });
    }

    public SpectGridDBData(Patients patients, Collection<Target> targets) {
        this.patients = patients;
        this.targets = targets;
        this.stud = Stud.builder()
                .n(-1L)
                .nbc_patients_n(patients.getN())
                .study_type(11L)
                .build();
        this.followUp = FollowUp.builder()
                .n(-1L)
                .build();
        this.target = Target.builder()
                .nbc_patients_n(patients.getN())
                .targetName("Мишень не")
                .targetTypeText("выбрана")
                .build();
        this.dataList = new ArrayList<>();
        this.studInj = StudInj.builder()
                .n(-1L)
                .build();
        dataMap = new HashMap<>();
    }

    public SpectGridData getSpectGridData() {
        SpectGridData spectGridData = new SpectGridData(this);
        spectGridData.setName(patients.getPeople().getName());
        spectGridData.setSurname(patients.getPeople().getSurname());
        spectGridData.setPatronymic(patients.getPeople().getPatronymic());
        spectGridData.setTarget(target);
        spectGridData.setCaseHistoryNum(String.valueOf(patients.getCase_history_num()));
        spectGridData.setDose(studInj.getInj_activity_bq());
        if (stud != null && stud.getStudydatetime() != null)
            spectGridData.setStudyDate(DateUtils.asLocalDate(stud.getStudydatetime()));
        FlupSpectData hyp = dataMap.get(HYP.toString() + SPHERE.toString());
        FlupSpectData hizSphere = dataMap.get(HIZ.toString() + SPHERE.toString());
        FlupSpectData hizIsoline10 = dataMap.get(HIZ.toString() + ISOLYNE10.toString());
        FlupSpectData hizIsoline25 = dataMap.get(HIZ.toString() + ISOLYNE25.toString());
        FlupSpectData targetSphere = dataMap.get(TARGET.toString() + SPHERE.toString());
        FlupSpectData targetIsoline10 = dataMap.get(TARGET.toString() + ISOLYNE10.toString());
        FlupSpectData targetIsoline25 = dataMap.get(TARGET.toString() + ISOLYNE25.toString());
        if (hyp != null) {
            spectGridData.setHypVolume(hyp.getVolume());
            spectGridData.setHypMin30(hyp.getEarly_phase());
            spectGridData.setHypMin60(hyp.getLate_phase());
        }

        if (hizSphere != null) {
            spectGridData.setHizSphereVolume(hizSphere.getVolume());
            spectGridData.setHizSphereMin30(hizSphere.getEarly_phase());
            spectGridData.setHizSphereMin60(hizSphere.getLate_phase());
        }

        if (hizIsoline10 != null) {
            spectGridData.setHizIsoline10Volume(hizIsoline10.getVolume());
            spectGridData.setHizIsoline10Min30(hizIsoline10.getEarly_phase());
            spectGridData.setHizIsoline10Min60(hizIsoline10.getLate_phase());
        }

        if (hizIsoline25 != null) {
            spectGridData.setHizIsoline25Volume(hizIsoline25.getVolume());
            spectGridData.setHizIsoline25Min30(hizIsoline25.getEarly_phase());
            spectGridData.setHizIsoline25Min60(hizIsoline25.getLate_phase());
        }

        if (targetSphere != null) {
            spectGridData.setTargetSphereVolume(targetSphere.getVolume());
            spectGridData.setTargetSphereMin30(targetSphere.getEarly_phase());
            spectGridData.setTargetSphereMin60(targetSphere.getLate_phase());
        }

        if (targetIsoline10 != null) {
            spectGridData.setTargetIsoline10Volume(targetIsoline10.getVolume());
            spectGridData.setTargetIsoline10Min30(targetIsoline10.getEarly_phase());
            spectGridData.setTargetIsoline10Min60(targetIsoline10.getLate_phase());
        }

        if (targetIsoline25 != null) {
            spectGridData.setTargetIsoline25Volume(targetIsoline25.getVolume());
            spectGridData.setTargetIsoline25Min30(targetIsoline25.getEarly_phase());
            spectGridData.setTargetIsoline25Min60(targetIsoline25.getLate_phase());
        }
        return spectGridData;
    }
}
