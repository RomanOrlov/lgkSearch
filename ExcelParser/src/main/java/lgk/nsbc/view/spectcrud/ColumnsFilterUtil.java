package lgk.nsbc.view.spectcrud;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.components.grid.HeaderCell;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.themes.ValoTheme;

public class ColumnsFilterUtil {
    public static void addTextFilter(HeaderRow filterHeader, Grid.Column<?, ?> column, ListDataProvider<SpectGridData> dataProvider) {
        HeaderCell cell = filterHeader.getCell(column);
        TextField filterField = getColumnTextFilterField();
        cell.setComponent(filterField);
        filterField.addValueChangeListener(event -> {
            String enteredValue = event.getValue();
            dataProvider.setFilter(SpectGridData::getSurname, surname -> {
                if (surname == null) return false;
                return surname.contains(enteredValue);
            });
        });
    }

    public static TextField getColumnTextFilterField() {
        TextField filter = new TextField();
        filter.setWidth("100%");
        filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
        filter.setPlaceholder("Фильтр");
        return filter;
    }
}
