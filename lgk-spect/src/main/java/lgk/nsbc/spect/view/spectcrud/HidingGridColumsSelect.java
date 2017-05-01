package lgk.nsbc.spect.view.spectcrud;

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
    private final SpectGrid spectGrid;

    public HidingGridColumsSelect(SpectGrid spectGrid) {
        this.spectGrid = spectGrid;
        Set<String> filters = new TreeSet<>();
        filters.addAll(ContourType.getNames());
        filters.addAll(MainInfo.getNames());
        filters.addAll(TargetType.getNames());
        filters.add("ИН");
        setItems(filters);
        setLeftColumnCaption("Отображаемые столбцы");
        setRightColumnCaption("Скрытые столбцы");
        setWidth("450px");
        setHeight("125px");
        addValueChangeListener(valueChangeEvent -> updateColumnsVisibility(valueChangeEvent.getValue()));
    }

    /**
     * Управление видимостью группы столбцов. Крайне необходимо из за внушающего количества столбцов
     *
     * @param columnNames Имена групп стобцов которые надо сделать невидимыми
     */
    public void updateColumnsVisibility(Set<String> columnNames) {
        spectGrid.mainInfoColumns.forEach(column -> column.setHidden(false));
        spectGrid.inColumns.forEach(column -> column.setHidden(false));
        for (String columnName : columnNames) {
            switch (columnName) {
                case "Сфера":
                    spectGrid.sphereColumns.forEach(column -> column.setHidden(true));
                    break;
                case "Изолиния 10":
                    spectGrid.isoline10Columns.forEach(column -> column.setHidden(true));
                    break;
                case "Изолиния 25":
                    spectGrid.isoline25Columns.forEach(column -> column.setHidden(true));
                    break;
                case "Хороидальное сплетение":
                    spectGrid.hizColumns.forEach(column -> column.setHidden(true));
                    break;
                case "Опухоль":
                    spectGrid.targetsColumns.forEach(column -> column.setHidden(true));
                    break;
                case "Гипофиз":
                    spectGrid.hypColumns.forEach(column -> column.setHidden(true));
                    break;
                case "Объем":
                    spectGrid.volumeColumns.forEach(column -> column.setHidden(true));
                    break;
                case "30 минут":
                    spectGrid.min30Columns.forEach(column -> column.setHidden(true));
                    break;
                case "60 минут":
                    spectGrid.min60Columns.forEach(column -> column.setHidden(true));
                    break;
                case "ИН":
                    spectGrid.inColumns.forEach(column -> column.setHidden(true));
                    break;
            }
        }
    }
}
