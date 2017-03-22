package lgk.nsbc.util;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.TwinColSelect;
import lgk.nsbc.template.model.spect.ContourType;
import lgk.nsbc.template.model.spect.TargetType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class HidingGridColumsSelect extends TwinColSelect {
    private final SpectData spectData;
    // Key - имя фильтра, Value - паттерн propertyId по которому искать нужные столбцы
    // Фильтры работают для всех столбцов, чьи проперти не начинаются с #
    private final Map<String, String> filters = new HashMap<>();

    public HidingGridColumsSelect(SpectData spectData) {
        for (TargetType targetType : TargetType.values()) {
            filters.put(targetType.getName(), targetType.toString());
        }
        for (ContourType contourType : ContourType.values()) {
            filters.put(contourType.getName(), contourType.toString());
        }

        this.spectData = spectData;
        setContainerDataSource(new IndexedContainer(filters.keySet()));
        setLeftColumnCaption("Отображаемые столбцы");
        setRightColumnCaption("Скрытые столбцы");
        addValueChangeListener(valueChangeEvent -> {
            Set<String> value = (Set<String>) valueChangeEvent.getProperty().getValue();
            Set<String> propertyId = value.stream().map(filters::get).collect(toSet());
            spectData.updateVisibility(propertyId);
        });
        setWidth("100%");
        setHeight("100px");
    }
}
