package lgk.nsbc.spect.view.holes;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.VaadinSessionScope;
import org.springframework.stereotype.Service;

/**
 * Класс для заполнения "возможных" пробелов данных в базе.
 */
@Service
@VaadinSessionScope
public class MissingData implements View {
    // Дата хирургии
    // МРТ и данные о контроле

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
