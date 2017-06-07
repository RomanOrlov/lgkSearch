package lgk.nsbc.spect.view.holes;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.VaadinSessionScope;
import com.vaadin.ui.VerticalLayout;
import lgk.nsbc.spect.util.components.NavigationBar;
import lgk.nsbc.spect.util.components.SuggestionCombobox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Класс для заполнения "возможных" пробелов данных в базе.
 */
@Service
@VaadinSessionScope
public class MissingData extends VerticalLayout implements View {
    // Дата хирургии
    // МРТ и данные о контроле
    private NavigationBar navigationBar = new NavigationBar();
    @Autowired
    private SuggestionCombobox suggestionCombobox;

    @PostConstruct
    void init() {
        suggestionCombobox.focus();
        addComponents(suggestionCombobox, navigationBar);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
