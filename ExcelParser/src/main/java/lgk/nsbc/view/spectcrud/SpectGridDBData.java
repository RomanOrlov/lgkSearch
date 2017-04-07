package lgk.nsbc.view.spectcrud;

import lgk.nsbc.model.NbcPatients;
import lgk.nsbc.model.NbcFlupSpectData;
import lgk.nsbc.model.NbcFollowUp;
import lgk.nsbc.model.NbcStud;
import lgk.nsbc.model.NbcTarget;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static lgk.nsbc.model.spect.ContourType.*;
import static lgk.nsbc.model.spect.TargetType.*;

/**
 * Класс представляющий собой одну строчку данных.
 */
@Getter
public class SpectGridDBData {
    private final NbcPatients nbcPatients;
    private final NbcStud nbcStud;
    private final NbcFollowUp nbcFollowUp;
    private final NbcTarget nbcTarget;
    private final List<NbcFlupSpectData> dataList;
    private final Map<String, NbcFlupSpectData> dataMap;

    public SpectGridDBData(NbcPatients nbcPatients, NbcStud nbcStud, NbcFollowUp nbcFollowUp, NbcTarget nbcTarget, List<NbcFlupSpectData> dataList) {
        this.nbcPatients = nbcPatients;
        this.nbcStud = nbcStud;
        this.nbcFollowUp = nbcFollowUp;
        this.nbcTarget = nbcTarget;
        this.dataList = dataList;
        dataMap = new HashMap<>();
        // Намного облегчает дальнейший парсинг данных
        dataList.forEach(nbcFlupSpectData -> {
            String property = nbcFlupSpectData.getTargetType().toString() + nbcFlupSpectData.getContourType().toString();
            dataMap.put(property, nbcFlupSpectData);
        });
    }

    public SpectGridData getSpectGridData() {
        SpectGridData spectGridData = new SpectGridData(this);
        spectGridData.setName(nbcPatients.getBasPeople().getName());
        spectGridData.setSurname(nbcPatients.getBasPeople().getSurname());
        spectGridData.setPatronymic(nbcPatients.getBasPeople().getPatronymic());
        spectGridData.setTargetName(nbcTarget.getTargetname());
        spectGridData.setCaseHistoryNum(nbcPatients.getCase_history_num().toString());
        NbcFlupSpectData hyp = dataMap.get(HYP.toString() + SPHERE.toString());
        NbcFlupSpectData hizSphere = dataMap.get(HIZ.toString() + SPHERE.toString());
        NbcFlupSpectData hizIsoline10 = dataMap.get(HIZ.toString() + ISOLYNE10.toString());
        NbcFlupSpectData hizIsoline25 = dataMap.get(HIZ.toString() + ISOLYNE25.toString());
        NbcFlupSpectData targetSphere = dataMap.get(TARGET.toString() + SPHERE.toString());
        NbcFlupSpectData targetIsoline10 = dataMap.get(TARGET.toString() + ISOLYNE10.toString());
        NbcFlupSpectData targetIsoline25 = dataMap.get(TARGET.toString() + ISOLYNE25.toString());
        spectGridData.setHypVolume(hyp.getVolume());
        spectGridData.setHypMin30(hyp.getEarly_phase());
        spectGridData.setHypMin60(hyp.getLate_phase());

        spectGridData.setHizSphereVolume(hizSphere.getVolume());
        spectGridData.setHizSphereMin30(hizSphere.getEarly_phase());
        spectGridData.setHizSphereMin60(hizSphere.getLate_phase());

        spectGridData.setHizIsoline10Volume(hizIsoline10.getVolume());
        spectGridData.setHizIsoline10Min30(hizIsoline10.getEarly_phase());
        spectGridData.setHizIsoline10Min60(hizIsoline10.getLate_phase());

        spectGridData.setHizIsoline25Volume(hizIsoline25.getVolume());
        spectGridData.setHizIsoline25Min30(hizIsoline25.getEarly_phase());
        spectGridData.setHizIsoline25Min60(hizIsoline25.getLate_phase());

        spectGridData.setTargetSphereVolume(targetSphere.getVolume());
        spectGridData.setTargetSphereMin30(targetSphere.getEarly_phase());
        spectGridData.setTargetSphereMin60(targetSphere.getLate_phase());

        spectGridData.setTargetIsoline10Volume(targetIsoline10.getVolume());
        spectGridData.setTargetIsoline10Min30(targetIsoline10.getEarly_phase());
        spectGridData.setTargetIsoline10Min60(targetIsoline10.getLate_phase());

        spectGridData.setTargetIsoline25Volume(targetIsoline25.getVolume());
        spectGridData.setTargetIsoline25Min30(targetIsoline25.getEarly_phase());
        spectGridData.setTargetIsoline25Min60(targetIsoline25.getLate_phase());
        return spectGridData;
    }
}
