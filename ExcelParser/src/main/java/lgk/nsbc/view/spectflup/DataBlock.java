package lgk.nsbc.view.spectflup;

import com.vaadin.ui.HorizontalLayout;
import lgk.nsbc.template.model.NbcFlupSpectData;
import lgk.nsbc.template.model.spect.ContourType;
import lgk.nsbc.template.model.spect.TargetType;
import lgk.nsbc.view.spectflup.bind.DataUnitBind;

import java.util.ArrayList;
import java.util.List;

import static lgk.nsbc.template.model.spect.ContourType.*;

/**
 * DataUnit для сферы и всех изолиний
 */
class DataBlock extends HorizontalLayout {
    final DataUnit sphere = new DataUnit(SPHERE.getName());
    final DataUnit isolyne10 = new DataUnit(ISOLYNE10.getName());
    final DataUnit isolyne25 = new DataUnit(ISOLYNE25.getName());
    private final TargetType targetType;

    public DataBlock(TargetType targetType) {
        this.targetType = targetType;
        setWidth("100%");
        addComponents(sphere, isolyne10, isolyne25);
    }

    public List<NbcFlupSpectData> getListOfData() {
        List<NbcFlupSpectData> spectDataList = new ArrayList<>();
        NbcFlupSpectData sphereData = fromDataBind(sphere.bind.getBean(), SPHERE);
        NbcFlupSpectData isolyne10Data = fromDataBind(isolyne10.bind.getBean(), ISOLYNE10);
        NbcFlupSpectData isolyne25Data = fromDataBind(isolyne25.bind.getBean(), ISOLYNE25);
        spectDataList.add(sphereData);
        spectDataList.add(isolyne10Data);
        spectDataList.add(isolyne25Data);
        return spectDataList;
    }

    public void setListOfData(List<NbcFlupSpectData> dataList) {
        List<NbcFlupSpectData> targetDataList = targetType.getSublistOfTarget(dataList);
        NbcFlupSpectData sphereData = SPHERE.getDataOfContour(targetDataList);
        NbcFlupSpectData isolyne10Data = ISOLYNE10.getDataOfContour(targetDataList);
        NbcFlupSpectData isolyne25Data = ISOLYNE25.getDataOfContour(targetDataList);
        sphere.bind.readBean(fromSpectData(sphereData));
        isolyne10.bind.readBean(fromSpectData(isolyne10Data));
        isolyne25.bind.readBean(fromSpectData(isolyne25Data));
    }

    private DataUnitBind fromSpectData(NbcFlupSpectData nbcFlupSpectData) {
        return new DataUnitBind(nbcFlupSpectData.getN(),
                nbcFlupSpectData.getVolume(),
                nbcFlupSpectData.getEarly_phase(),
                nbcFlupSpectData.getLate_phase());
    }

    private NbcFlupSpectData fromDataBind(DataUnitBind dataUnitBind, ContourType contourType) {
        return NbcFlupSpectData.builder()
                .n(dataUnitBind.getN())
                .targetType(targetType)
                .contourType(contourType)
                .contourType(SPHERE)
                .volume(dataUnitBind.getVolume())
                .early_phase(dataUnitBind.getEarlyPhase())
                .late_phase(dataUnitBind.getLatePhase())
                .build();

    }
}
