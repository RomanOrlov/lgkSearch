package lgk.nsbc.util.components;

import com.vaadin.server.SerializableToIntFunction;
import com.vaadin.ui.ComboBox;
import lgk.nsbc.model.Patients;
import lgk.nsbc.model.dao.PatientsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@Component
@Scope(value = "prototype")
public class SuggestionCombobox extends ComboBox<Patients> {
    @Autowired
    private PatientsDao patientsDao;

    public SuggestionCombobox() {
        FetchItemsCallback<Patients> callback = (filter, offset, limit) -> {
            if (!isInputValid(filter)) return Stream.empty();
            return patientsDao.getPatientsWithFullNameLike(filter)
                    .stream()
                    .skip(offset)
                    .limit(limit);
        };
        SerializableToIntFunction<String> sizeCallback = value -> {
            if (!isInputValid(value)) return 0;
            return patientsDao.getPatientsWithFullNameLike(value).size();
        };
        setDataProvider(callback, sizeCallback);
        setCaption("Поиск пациента");
        setWidth("100%");
        setEmptySelectionAllowed(false);
        addValueChangeListener(valueChangeEvent -> setCaption("Выбран пациент " + valueChangeEvent.getValue().toStringWithCaseHistory()));
    }

    private boolean isInputValid(String value) {
        return value.trim().length() > 4 && !value.matches("[a-zA-Z0-9\\Q,.<>;:'\"[]{}\\E]+");
    }
}
