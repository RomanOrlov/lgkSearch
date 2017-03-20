package lgk.nsbc.util;

import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import lgk.nsbc.view.RepresentationName;

import java.util.List;
import java.util.function.Function;

/**
 * see http://blog.oio.de/2015/01/17/write-simple-auto-complete-combobox-vaadin/
 * Я реально не хочу писать сосбтвенный компонент.
 * @param <T>
 */
public class SuggestionCombobox<T extends RepresentationName> extends ComboBox {
    private final Function<String, List<T>> suggestionFilter;
    private final Class<T> tClass;
    private T lastSelectedBean;

    public SuggestionCombobox(Function<String, List<T>> suggestionFilter, Class<T> tClass) {
        this.suggestionFilter = suggestionFilter;
        this.tClass = tClass;
        SuggestionContainer<T> container = new SuggestionContainer<>(suggestionFilter, tClass);
        addValueChangeListener(event -> {
            Notification.show("Выбран пациент: " + event.getProperty().getValue(), Notification.Type.HUMANIZED_MESSAGE);
            T t = tClass.cast(event.getProperty().getValue());
            lastSelectedBean = t;
            container.setSelectedBean(t);
        });

        setCaption("Поиск пациента");
        setContainerDataSource(container);
        setNullSelectionAllowed(false);
        setItemCaptionPropertyId(ItemCaptionMode.PROPERTY);
        setItemCaptionPropertyId("representationName");
        setWidth("100%");
    }

    @Override
    protected Filter buildFilter(String filterString, FilteringMode filteringMode) {
        return new SuggestionContainer.SuggestionFilter(filterString);
    }

    public T getSelectedPatient() {
        return lastSelectedBean;
    }
}
