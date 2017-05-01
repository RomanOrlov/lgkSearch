package lgk.nsbc.spect.view.spectcrud;

import com.vaadin.server.SerializableToIntFunction;
import com.vaadin.ui.ComboBox;
import lgk.nsbc.model.NbcPatients;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class SuggestionCombobox extends ComboBox<NbcPatients> {

    public SuggestionCombobox(Function<String, List<NbcPatients>> suggestionFilter) {
        FetchItemsCallback<NbcPatients> callback = (filter, offset, limit) -> {

            if (!isInputValid(filter)) return Stream.empty();
            return suggestionFilter.apply(filter)
                    .stream()
                    .skip(offset)
                    .limit(limit);
        };
        SerializableToIntFunction<String> sizeCallback = value -> {
            if (!isInputValid(value)) return 0;
            return suggestionFilter.apply(value).size();
        };
        setDataProvider(callback, sizeCallback);
        setCaption("Поиск пациента");
        setWidth("100%");
        setEmptySelectionAllowed(false);
    }

    private boolean isInputValid(String value) {
        return value.trim().length() > 4 && !value.matches("[a-zA-Z0-9\\Q,.<>;:'\"[]{}\\E]+");
    }
}
