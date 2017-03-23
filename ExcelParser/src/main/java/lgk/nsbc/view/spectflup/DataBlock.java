package lgk.nsbc.view.spectflup;

import com.vaadin.ui.HorizontalLayout;
import lgk.nsbc.template.model.NbcFlupSpectData;
import lgk.nsbc.template.model.spect.ContourType;
import lgk.nsbc.template.model.spect.TargetType;

import java.util.ArrayList;
import java.util.List;

import static lgk.nsbc.template.model.spect.ContourType.*;

/**
 * DataUnit для сферы и всех изолиний
 */
class DataBlock extends HorizontalLayout {
    private final DataUnit sphere = new DataUnit(SPHERE.getName());
    private final DataUnit isolyne10 = new DataUnit(ISOLYNE10.getName());
    private final DataUnit isolyne25 = new DataUnit(ISOLYNE25.getName());
    private final TargetType targetType;

    public DataBlock(TargetType targetType) {
        this.targetType = targetType;
        //setCaption(targetType.getName());
        setWidth("100%");
        setHeight("40px");
        setSpacing(true);
        addComponents(sphere, isolyne10, isolyne25);
        setExpandRatio(sphere, 1.0f);
        setExpandRatio(isolyne10, 1.0f);
        setExpandRatio(isolyne25, 1.0f);
    }

    public List<NbcFlupSpectData> getListOfData() {
        List<NbcFlupSpectData> spectDataList = new ArrayList<>();
        // Сфера
        NbcFlupSpectData sphereData = sphere.getSpectData();
        sphereData.setContour_size(1L);
        sphereData.setContour_type(SPHERE.getName());
        // Изолиния 10
        NbcFlupSpectData isolyne10Data = isolyne10.getSpectData();
        isolyne10Data.setContour_size(10L);
        isolyne10Data.setContour_type(ContourType.ISOLYNE10.getName());
        // Изолиния 25
        NbcFlupSpectData isolyne25Data = isolyne25.getSpectData();
        isolyne25Data.setContour_size(25L);
        isolyne25Data.setContour_type(ContourType.ISOLYNE25.getName());
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
        sphere.setSpectData(sphereData);
        isolyne10.setSpectData(isolyne10Data);
        isolyne25.setSpectData(isolyne25Data);
    }
}
