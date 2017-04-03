package lgk.nsbc.view.spectcrud;

import com.vaadin.ui.TwinColSelect;
import lgk.nsbc.model.spect.ContourType;
import lgk.nsbc.model.spect.TargetType;
import lgk.nsbc.view.spectcrud.SpectData;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class HidingGridColumsSelect extends TwinColSelect<String> {
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
        setItems(filters.keySet());
        setLeftColumnCaption("Отображаемые столбцы");
        setRightColumnCaption("Скрытые столбцы");
        addValueChangeListener(valueChangeEvent -> {
            Set<String> selectedId = valueChangeEvent.getValue()
                    .stream()
                    .map(filters::get)
                    .collect(toSet());
            spectData.updateVisibility(selectedId);
        });
        setWidth("100%");
        setHeightUndefined();
    }
}
