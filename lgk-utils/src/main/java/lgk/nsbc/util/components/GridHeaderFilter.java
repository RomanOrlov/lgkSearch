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
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static com.vaadin.server.SerializableFunction.identity;

public class GridHeaderFilter {
    private static final String doubleRegex = "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?";
    private static final String integerRegex = "(?<=\\s|^)\\d+(?=\\s|$)";
    private static final Predicate<String> emptyString = s -> s != null && !s.trim().isEmpty();

    public static <T> void addTextFilter(HeaderCell cell,
                                         ListDataProvider<T> dataProvider,
                                         ValueProvider<T, String> valueProvider,
                                         GlobalGridFilter<T> globalGridFilter) {
        TextField filterField = getColumnTextFilterField();
        addStringFilterChain(filterField,
                valueProvider,
                globalGridFilter,
                (s, s2) -> s2.contains(s.trim()));
        HorizontalLayout components = wrapWithClearButton(filterField);
        cell.setComponent(components);
        filterField.addValueChangeListener(event -> dataProvider.setFilter(globalGridFilter::test));
    }

    public static <T> void addIntegerFilter(HeaderCell cell,
                                            ListDataProvider<T> dataProvider,
                                            ValueProvider<T, Integer> valueProvider,
                                            GlobalGridFilter<T> globalGridFilter) {
        TextField from = getColumnNumberFilterField(true);
        TextField to = getColumnNumberFilterField(false);
        HorizontalLayout components = wrapWithClearButton(from, to);
        cell.setComponent(components);
        addIntegerFilterChain(from,
                valueProvider,
                globalGridFilter,
                (fromValue, providerValue) -> providerValue != null && fromValue < providerValue
        );
        addIntegerFilterChain(to,
                valueProvider,
                globalGridFilter,
                (toValue, providerValue) -> providerValue != null && toValue > providerValue
        );
        HasValue.ValueChangeListener<String> listener = event -> dataProvider.setFilter(globalGridFilter::test);
        from.addValueChangeListener(listener);
        to.addValueChangeListener(listener);
    }


    public static <T> void addDoubleFilter(HeaderCell cell,
                                           ListDataProvider<T> dataProvider,
                                           ValueProvider<T, Double> valueProvider,
                                           GlobalGridFilter<T> globalGridFilter) {
        TextField from = getColumnNumberFilterField(true);
        TextField to = getColumnNumberFilterField(false);
        HorizontalLayout components = wrapWithClearButton(from, to);
        cell.setComponent(components);
        addDoubleFilterChain(from,
                valueProvider,
                globalGridFilter,
                (fromValue, providerValue) -> providerValue != null && fromValue < providerValue
        );
        addDoubleFilterChain(to,
                valueProvider,
                globalGridFilter,
                (toValue, providerValue) -> providerValue != null && toValue > providerValue
        );
        HasValue.ValueChangeListener<String> listener = event -> dataProvider.setFilter(globalGridFilter::test);
        from.addValueChangeListener(listener);
        to.addValueChangeListener(listener);
    }

    public static <T, V> void addListSelectFilter(HeaderCell cell,
                                                  ListDataProvider<T> dataProvider,
                                                  ValueProvider<T, V> valueProvider,
                                                  Collection<V> options,
                                                  GlobalGridFilter<T> globalGridFilter) {
        ComboBox<V> listSelect = getComboboxFilterField(options);
        HorizontalLayout layout = wrapWithClearButton(listSelect);
        cell.setComponent(layout);
        addObjectFilterChain(listSelect,
                valueProvider,
                globalGridFilter,
                Objects::equals);
        listSelect.addValueChangeListener(event -> {
            if (Objects.equals(event.getOldValue(),event.getValue()))
                return;
            dataProvider.setFilter(globalGridFilter::test);
        });
    }

    public static <T, V> void addRadioButtonFilter(HeaderCell cell,
                                                   ListDataProvider<T> dataProvider,
                                                   ValueProvider<T, V> valueProvider,
                                                   Collection<V> options,
                                                   GlobalGridFilter<T> globalGridFilter) {
        RadioButtonGroup<V> radioButtonFilterField = getRadioButtonFilterField(options);
        HorizontalLayout components = wrapWithClearButton(radioButtonFilterField);
        cell.setComponent(components);
        radioButtonFilterField.getDataProvider().refreshAll();
        addObjectFilterChain(radioButtonFilterField,
                valueProvider,
                globalGridFilter,
                Objects::equals);
        radioButtonFilterField.addValueChangeListener(event -> {
            if (Objects.equals(event.getOldValue(),event.getValue()))
                return;
            dataProvider.setFilter(globalGridFilter::test);
        });
    }

    public static <T, V> void addCheckBoxFilter(HeaderCell cell,
                                                ListDataProvider<T> dataProvider,
                                                ValueProvider<T, V> valueProvider,
                                                V option,
                                                GlobalGridFilter<T> globalGridFilter) {
        CheckBox checkBox = getCheckBoxFilterField();
        cell.setComponent(checkBox);
        addBooleanFilterChain(checkBox,
                valueProvider,
                globalGridFilter,
                option);
        checkBox.addValueChangeListener(valueChangeEvent -> dataProvider.setFilter(globalGridFilter::test));
    }

    private static HorizontalLayout wrapWithClearButton(AbstractField... abstractField) {
        Button clear = getClearButton();
        clear.addClickListener(clickEvent -> Arrays.asList(abstractField).forEach(HasValue::clear));
        HorizontalLayout components = new HorizontalLayout(abstractField);
        components.addComponent(clear);
        return components;
    }

    private static HorizontalLayout wrapWithClearButton(AbstractSingleSelect abstractSingleSelect) {
        Button clear = getClearButton();
        clear.addClickListener(clickEvent -> {
            abstractSingleSelect.clear();
            abstractSingleSelect.getDataProvider().refreshAll();
        });
        HorizontalLayout components = new HorizontalLayout(abstractSingleSelect, clear);
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
        filter.setWidth("45px");
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
        checkBox.setIcon(VaadinIcons.FILTER);
        return checkBox;
    }

    private static <T> RadioButtonGroup<T> getRadioButtonFilterField(Collection<T> options) {
        RadioButtonGroup<T> radioButtonGroup = new RadioButtonGroup<>();
        radioButtonGroup.setStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        radioButtonGroup.setItems(options);
        return radioButtonGroup;
    }

    private static <T, V> void addObjectFilterChain(HasValue<T> hasValue,
                                                    ValueProvider<V, T> valueProvider,
                                                    GlobalGridFilter<V> globalGridFilter,
                                                    BiFunction<T, T, Boolean> filter) {
        FilterChain<T, T, V> filterChain = new FilterChain<>(hasValue,
                Objects::nonNull,
                identity(),
                valueProvider,
                filter);
        globalGridFilter.addFilter(filterChain);
    }

    private static <T> void addStringFilterChain(HasValue<String> hasValue,
                                                 ValueProvider<T, String> valueProvider,
                                                 GlobalGridFilter<T> globalGridFilter,
                                                 BiFunction<String, String, Boolean> filter) {
        FilterChain<String, String, T> filterChain = new FilterChain<>(hasValue,
                emptyString,
                identity(),
                valueProvider,
                filter);
        globalGridFilter.addFilter(filterChain);
    }

    private static <T> void addIntegerFilterChain(HasValue<String> hasValue,
                                                  ValueProvider<T, Integer> valueProvider,
                                                  GlobalGridFilter<T> globalGridFilter,
                                                  BiFunction<Integer, Integer, Boolean> filter) {
        FilterChain<String, Integer, T> filterChain = new FilterChain<>(hasValue,
                emptyString.and(s -> s.matches(integerRegex)),
                Integer::valueOf,
                valueProvider,
                filter);
        globalGridFilter.addFilter(filterChain);
    }

    private static <T> void addDoubleFilterChain(HasValue<String> hasValue,
                                                 ValueProvider<T, Double> valueProvider,
                                                 GlobalGridFilter<T> globalGridFilter,
                                                 BiFunction<Double, Double, Boolean> filter) {
        FilterChain<String, Double, T> filterChain = new FilterChain<>(hasValue,
                emptyString.and(s -> s.matches(doubleRegex)),
                Double::valueOf,
                valueProvider,
                filter);
        globalGridFilter.addFilter(filterChain);
    }

    private static <T, V> void addBooleanFilterChain(HasValue<Boolean> hasValue,
                                                     ValueProvider<V, T> valueProvider,
                                                     GlobalGridFilter<V> globalGridFilter,
                                                     T option) {
        FilterChain<Boolean, T, V> filterChain = new FilterChain<>(hasValue,
                aBoolean -> aBoolean,
                t -> option,
                valueProvider,
                Objects::equals);
        globalGridFilter.addFilter(filterChain);
    }
}
