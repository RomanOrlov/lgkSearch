package lgk.nsbc.spect.view;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import lgk.nsbc.histology.HistologyView;
import lgk.nsbc.spect.view.spectcrud.SpectCRUDView;
import lgk.nsbc.spect.view.statistic.SampleStatistic;
import org.springframework.beans.factory.annotation.Autowired;

@Title("Грант ОФЕКТ")
@Theme("valo")
@SpringUI
public class MainSpectView extends UI {
    @Autowired
    private SpectCRUDView spectCRUDView;
    @Autowired
    private LoginPage loginPage;
    @Autowired
    private HistologyView histologyView;
    @Autowired
    private SampleStatistic sampleStatistic;

    public static final String LOGIN = "login";
    public static final String SPECT_VIEW = "spect";
    public static final String HISTOLOGY_VIEW = "histology";
    public static final String STATISTIC_VIEW = "statistic";

    private Navigator navigator;

    @Override
    protected void init(VaadinRequest request) {
        navigator = new Navigator(this, this);
        navigator.addView(LOGIN, loginPage);
        navigator.addView(SPECT_VIEW, spectCRUDView);
        navigator.addView(HISTOLOGY_VIEW, histologyView);
        navigator.addView(STATISTIC_VIEW, sampleStatistic);
        navigator.navigateTo(STATISTIC_VIEW);
    }
}
