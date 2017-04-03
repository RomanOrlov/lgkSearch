package lgk.nsbc.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.VaadinSessionScope;
import com.vaadin.ui.LoginForm;
import org.springframework.stereotype.Service;

import static lgk.nsbc.view.MainSpectView.MAIN_VIEW;

@Service
@VaadinSessionScope
public class LoginPage extends LoginForm implements View {
    public LoginPage() {
        /*
        1. Проверяем не в системе ли пользователь.
        2. Если в системе, переводим на главную страницу (ну и вытаскиваем сессию)
        3. Если не в системе, переводим на страницу авторизации, авторизуемся, регистрируем сессию.
         */
        addLoginListener(event -> getUI().getNavigator().navigateTo(MAIN_VIEW));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
