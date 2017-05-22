package lgk.nsbc.util.components;

import com.vaadin.data.HasValue;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.components.grid.HeaderCell;
import com.vaadin.ui.themes.ValoTheme;

public class GridHeaderFilter {
    public static <T> void addTextFilter(HeaderCell cell,
                                         ListDataProvider<T> dataProvider,
                                         ValueProvider<T, String> valueProvider) {
        TextField filterField = getColumnTextFilterField();
        cell.setComponent(filterField);
        filterField.addValueChangeListener(event -> {
            String enteredValue = event.getValue();
            dataProvider.setFilter(valueProvider, text -> text != null && text.contains(enteredValue));
        });
    }

    public static <T> void addIntegerFilter(HeaderCell cell,
                                            ListDataProvider<T> dataProvider,
                                            ValueProvider<T, Integer> valueProvider) {
        TextField from = getColumnNumberFilterField(true);
        TextField to = getColumnNumberFilterField(false);
        HorizontalLayout fromTo = new HorizontalLayout(from, to);
        cell.setComponent(fromTo);
        HasValue.ValueChangeListener<String> listener = event -> {
            String fromString = from.getValue() == null ? "" : from.getValue().trim();
            String toString = to.getValue() == null ? "" : to.getValue().trim();
            if (fromString.matches("[0-9]*") && fromString.matches("[0-9]*")) {
                Integer fromInt = fromString.isEmpty() ? Integer.MIN_VALUE : Integer.parseInt(fromString);
                Integer toInt = toString.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(toString);
                if (fromInt < toInt)
                    dataProvider.setFilter(valueProvider, integer -> integer == null || (integer >= fromInt && integer <= toInt));
            }
        };
        from.addValueChangeListener(listener);
        to.addValueChangeListener(listener);
    }

    private static TextField getColumnTextFilterField() {
        TextField filter = new TextField();
        filter.setWidth("100%");
        filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
        filter.setPlaceholder("Фильтр");
        return filter;
    }

    private static TextField getColumnNumberFilterField(boolean isFrom) {
        TextField filter = new TextField();
        filter.setWidth("60px");
        filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
        filter.setPlaceholder(isFrom ? "От" : "До");
        return filter;
    }
}
