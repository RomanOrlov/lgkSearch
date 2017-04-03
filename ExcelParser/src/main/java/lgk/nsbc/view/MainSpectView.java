package lgk.nsbc.view;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

@Title("Excel upload")
@Theme("valo")
@SpringUI
public class MainSpectView extends UI {
    @Autowired
    private SpectCRUDView spectCRUDView;
    @Autowired
    private LoginPage loginPage;
    public static final String LOGIN = "login";
    public static final String MAIN_VIEW = "main";

    private Navigator navigator;

    @Override
    protected void init(VaadinRequest request) {
        navigator = new Navigator(this, this);
        navigator.addView(LOGIN, loginPage);
        navigator.addView(MAIN_VIEW, spectCRUDView);
        navigator.navigateTo(LOGIN);
    }
}
