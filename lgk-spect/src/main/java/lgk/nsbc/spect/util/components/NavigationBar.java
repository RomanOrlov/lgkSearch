package lgk.nsbc.spect.util.components;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.stereotype.Component;

import static lgk.nsbc.spect.view.MainSpectView.*;

@Component
public class NavigationBar extends HorizontalLayout {
    private Button spectData = new Button("Данные ОФЕКТ");
    private Button histology = new Button("Данные гистологии");
    private Button statistic = new Button("Статистика выборки");
    private Button holes = new Button("Добавление процедур");
    private Button logout = new Button("Выход!");

    public NavigationBar() {
        setWidth("100%");
        setMargin(false);

        spectData.addClickListener(clickEvent -> getUI().getNavigator().navigateTo(SPECT_VIEW));
        spectData.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        spectData.setWidthUndefined();

        histology.addClickListener(clickEvent -> getUI().getNavigator().navigateTo(HISTOLOGY_VIEW));
        histology.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        histology.setWidthUndefined();

        statistic.addClickListener(clickEvent -> getUI().getNavigator().navigateTo(STATISTIC_VIEW));
        statistic.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        statistic.setWidthUndefined();

        holes.addClickListener(clickEvent -> getUI().getNavigator().navigateTo(MISSING_DATA));
        holes.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        holes.setWidthUndefined();

        logout.addClickListener(clickEvent -> {
            UI.getCurrent().getSession().close();
            UI.getCurrent().getPage().setUriFragment("#!login");
            //getUI().getNavigator().navigateTo(LOGIN);
        });
        logout.setStyleName(ValoTheme.BUTTON_PRIMARY);
        logout.setIcon(VaadinIcons.EXIT);
        logout.setWidthUndefined();

        addComponents(spectData, histology, statistic, holes, logout);
    }
}
