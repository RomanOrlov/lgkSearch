package lgk.nsbc.view;

import com.vaadin.spring.annotation.VaadinSessionScope;
import com.vaadin.ui.LoginForm;
import org.springframework.stereotype.Service;

@Service
@VaadinSessionScope
public class LoginPage extends LoginForm {
    public LoginPage() {
        /*
        1. Проверяем не в системе ли пользователь.
        2. Если в системе, переводим на главную страницу (ну и вытаскиваем сессию)
        3. Если не в системе, переводим на страницу авторизации, авторизуемся, регистрируем сессию.
         */
    }
}
