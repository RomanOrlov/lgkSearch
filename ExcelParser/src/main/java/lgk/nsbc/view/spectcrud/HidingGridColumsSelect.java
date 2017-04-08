package lgk.nsbc.view.spectcrud;

import com.vaadin.ui.TwinColSelect;
import lgk.nsbc.model.spect.ContourType;
import lgk.nsbc.model.spect.MainInfo;
import lgk.nsbc.model.spect.TargetType;

import java.util.Set;
import java.util.TreeSet;

/**
 * Класс необходимый для настройки видимости групп столбцов
 */
public class HidingGridColumsSelect extends TwinColSelect<String> {

    public HidingGridColumsSelect(SpectGrid spectGrid) {
        Set<String> filters = new TreeSet<>();
        filters.addAll(ContourType.getNames());
        filters.addAll(MainInfo.getNames());
        filters.addAll(TargetType.getNames());
        filters.add("ИН");
        setItems(filters);
        setLeftColumnCaption("Отображаемые столбцы");
        setRightColumnCaption("Скрытые столбцы");
        setWidth("450px");
        setHeight("150px");
        addValueChangeListener(valueChangeEvent -> spectGrid.updateColumnsVisibility(valueChangeEvent.getValue()));
    }
}
