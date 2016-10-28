package lgk.nsbc.view;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class Util {
    /**
     * Добавление фильтра по столбцу
     * Взято с Vaadin Sample про Grid
     * @param columnId Id столбца
     * @return Фильтр, - текстовое поле
     */
    public static TextField getColumnFilter(Grid grid, final Object columnId) {
        TextField filter = new TextField();
        filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
        filter.setInputPrompt("Фильтр");
        filter.setWidth("100%");
        filter.addTextChangeListener(new FieldEvents.TextChangeListener() {
            SimpleStringFilter filter = null;

            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                Container.Filterable f = (Container.Filterable) grid.getContainerDataSource();
                // Удалить старый фильтр
                if (filter != null) {
                    f.removeContainerFilter(filter);
                }
                // Установить новый фильтр по имени columnId
                filter = new SimpleStringFilter(columnId, event.getText(), true, true);
                f.addContainerFilter(filter);
                grid.cancelEditor();
            }
        });
        return filter;
    }
}
