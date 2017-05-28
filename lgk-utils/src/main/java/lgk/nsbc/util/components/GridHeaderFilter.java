package lgk.nsbc.util.components;

import com.vaadin.data.HasValue;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.HeaderCell;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class GridHeaderFilter {
    private static final String doubleRegex = "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?";
    private static final String integerRegex = "(?<=\\s|^)\\d+(?=\\s|$)";

    public static <T> void addTextFilter(HeaderCell cell,
                                         ListDataProvider<T> dataProvider,
                                         ValueProvider<T, String> valueProvider) {
        TextField filterField = getColumnTextFilterField();
        HorizontalLayout components = wrapWithClearButton(filterField);
        cell.setComponent(components);
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
        HorizontalLayout components = wrapWithClearButton(from, to);
        cell.setComponent(components);
        //HorizontalLayout fromTo = new HorizontalLayout(from, to);
        //cell.setComponent(fromTo);
        HasValue.ValueChangeListener<String> listener = event -> {
            String fromString = from.getValue() == null ? "" : from.getValue().trim();
            String toString = to.getValue() == null ? "" : to.getValue().trim();
            if (fromString.matches(integerRegex) && fromString.matches(integerRegex)) {
                Integer fromInt = fromString.isEmpty() ? Integer.MIN_VALUE : Integer.parseInt(fromString);
                Integer toInt = toString.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(toString);
                if (fromInt < toInt)
                    dataProvider.setFilter(valueProvider, integer -> integer == null || (integer >= fromInt && integer <= toInt));
            }
        };
        from.addValueChangeListener(listener);
        to.addValueChangeListener(listener);
    }

    public static <T> void addDoubleFilter(HeaderCell cell,
                                           ListDataProvider<T> dataProvider,
                                           ValueProvider<T, Double> valueProvider) {
        TextField from = getColumnNumberFilterField(true);
        TextField to = getColumnNumberFilterField(false);
        HorizontalLayout components = wrapWithClearButton(from, to);
        cell.setComponent(components);
        //HorizontalLayout fromTo = new HorizontalLayout(from, to);
        //cell.setComponent(fromTo);
        HasValue.ValueChangeListener<String> listener = event -> {
            String fromString = from.getValue() == null ? "" : from.getValue().trim();
            String toString = to.getValue() == null ? "" : to.getValue().trim();
            if (fromString.matches(doubleRegex) && fromString.matches(doubleRegex)) {
                Double fromDouble = fromString.isEmpty() ? Double.MIN_VALUE : Double.parseDouble(fromString);
                Double toDouble = toString.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(toString);
                if (fromDouble < toDouble)
                    dataProvider.setFilter(valueProvider, doubleVal -> doubleVal == null || (doubleVal >= fromDouble && doubleVal <= toDouble));
            }
        };
        from.addValueChangeListener(listener);
        to.addValueChangeListener(listener);
    }

    public static <T, V> void addListSelectFilter(HeaderCell cell,
                                                  ListDataProvider<T> dataProvider,
                                                  ValueProvider<T, V> valueProvider,
                                                  Collection<V> options) {
        ComboBox<V> listSelect = getComboboxFilterField(options);
        cell.setComponent(listSelect);
        listSelect.addValueChangeListener(event -> {
            V value = event.getValue();
            dataProvider.setFilter(valueProvider, v -> value == null || v == null || value.equals(v));
        });
    }

    public static <T, V> void addRadioButtonFilter(HeaderCell cell,
                                                   ListDataProvider<T> dataProvider,
                                                   ValueProvider<T, V> valueProvider,
                                                   Collection<V> options) {
        RadioButtonGroup<V> radioButtonFilterField = getRadioButtonFilterField(options);
        HorizontalLayout components = wrapWithClearButton(radioButtonFilterField);
        cell.setComponent(components);
        radioButtonFilterField.addValueChangeListener(event -> {
            V value = event.getValue();
            dataProvider.setFilter(valueProvider, v -> value != null && value.equals(v));
        });
    }

    public static <T, V> void addCheckBoxFilter(HeaderCell cell,
                                                ListDataProvider<T> dataProvider,
                                                ValueProvider<T, V> valueProvider,
                                                V option) {
        CheckBox checkBox = getCheckBoxFilterField();
        cell.setComponent(checkBox);
        checkBox.addValueChangeListener(valueChangeEvent -> dataProvider.setFilter(valueProvider, v -> !valueChangeEvent.getValue() || Objects.equals(v, option)));
    }

    private static HorizontalLayout wrapWithClearButton(AbstractField... abstractField) {
        Button clear = getClearButton();
        clear.addClickListener(clickEvent -> Arrays.asList(abstractField).forEach(HasValue::clear));
        HorizontalLayout components = new HorizontalLayout(abstractField);
        components.addComponent(clear);
        return components;
    }

    private static HorizontalLayout wrapWithClearButton(RadioButtonGroup radioButtonGroup) {
        Button clear = getClearButton();
        clear.addClickListener(clickEvent -> radioButtonGroup.clear());
        HorizontalLayout components = new HorizontalLayout(radioButtonGroup, clear);
        return components;
    }

    private static Button getClearButton() {
        Button clear = new Button();
        clear.setStyleName(ValoTheme.BUTTON_TINY);
        clear.setIcon(VaadinIcons.CLOSE_SMALL);
        return clear;
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
        filter.setWidth("70px");
        filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
        filter.setPlaceholder(isFrom ? "От" : "До");
        return filter;
    }

    private static <T> ComboBox<T> getComboboxFilterField(Collection<T> options) {
        ComboBox<T> comboBox = new ComboBox<>();
        comboBox.setPlaceholder("Фильтр");
        comboBox.setItems(options);
        comboBox.setWidth("100%");
        comboBox.setEmptySelectionAllowed(true);
        comboBox.addStyleName(ValoTheme.COMBOBOX_TINY);
        return comboBox;
    }

    private static CheckBox getCheckBoxFilterField() {
        CheckBox checkBox = new CheckBox();
        checkBox.setStyleName(ValoTheme.CHECKBOX_SMALL);
        return checkBox;
    }

    private static <T> RadioButtonGroup<T> getRadioButtonFilterField(Collection<T> options) {
        RadioButtonGroup<T> radioButtonGroup = new RadioButtonGroup<>();
        radioButtonGroup.setStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        radioButtonGroup.setItems(options);
        return radioButtonGroup;
    }
}
