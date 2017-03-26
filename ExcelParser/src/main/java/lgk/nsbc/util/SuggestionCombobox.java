package lgk.nsbc.util;

import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Notification;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * see http://blog.oio.de/2015/01/17/write-simple-auto-complete-combobox-vaadin/
 * Я реально не хочу писать сосбтвенный компонент.
 * @param <T>
 */
public class SuggestionCombobox<T> extends ComboBox {
    private final Class<T> tClass;
    private T lastSelectedBean;

    public SuggestionCombobox(Class<T> tClass,
                              Function<String, List<T>> suggestionFilter,
                              Function<T, Optional<T>> duplicatesResolver) {
        this.tClass = tClass;
        SuggestionContainer<T> container = new SuggestionContainer<>(suggestionFilter, tClass);
        addValueChangeListener(event -> {
            Notification.show("Выбран пациент: " + event.getProperty().getValue(), Notification.Type.HUMANIZED_MESSAGE);
            T t = tClass.cast(event.getProperty().getValue());
            lastSelectedBean = t;
            T rightTarget = duplicatesResolver.apply(t).get();
            container.setSelectedBean(rightTarget);
        });

        setCaption("Поиск пациента");
        setContainerDataSource(container);
        setNullSelectionAllowed(false);
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
