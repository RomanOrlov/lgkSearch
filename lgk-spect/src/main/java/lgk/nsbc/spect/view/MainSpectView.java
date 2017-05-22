package lgk.nsbc.spect.view;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import lgk.nsbc.histology.HistologyView;
import lgk.nsbc.spect.view.LoginPage;
import org.springframework.beans.factory.annotation.Autowired;

@Title("Testing")
@Theme("valo")
@SpringUI
public class MainSpectView extends UI {
    @Autowired
    private SpectCRUDView spectCRUDView;
    @Autowired
    private LoginPage loginPage;
    @Autowired
    private HistologyView histologyView;

    public static final String LOGIN = "login";
    public static final String MAIN_VIEW = "main";
    public static final String HISTOLOGY_VIEW = "histology";

    private Navigator navigator;

    @Override
    protected void init(VaadinRequest request) {
        navigator = new Navigator(this, this);
        navigator.addView(LOGIN, loginPage);
        navigator.addView(MAIN_VIEW, spectCRUDView);
        navigator.addView(HISTOLOGY_VIEW, histologyView);
        //navigator.navigateTo(LOGIN);
        navigator.navigateTo(HISTOLOGY_VIEW);
    }
}
