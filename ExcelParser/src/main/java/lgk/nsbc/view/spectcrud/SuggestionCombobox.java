package lgk.nsbc.view.spectcrud;

import com.vaadin.server.SerializableToIntFunction;
import com.vaadin.ui.ComboBox;
import lgk.nsbc.model.NbcPatients;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class SuggestionCombobox extends ComboBox<NbcPatients> {

    public SuggestionCombobox(Function<String, List<NbcPatients>> suggestionFilter) {
        FetchItemsCallback<NbcPatients> callback = (filter, offset, limit) -> {
            if (filter.length() < 3 || filter.matches("[a-zA-Z\\Q,.<>;:'\"[]{}\\E]+")) return Stream.empty();
            return suggestionFilter.apply(filter)
                    .stream()
                    .skip(offset)
                    .limit(limit);
        };
        SerializableToIntFunction<String> sizeCallback = value -> suggestionFilter.apply(value).size();
        setDataProvider(callback, sizeCallback);
        setCaption("Поиск пациента");
        setWidth("100%");
    }
}
