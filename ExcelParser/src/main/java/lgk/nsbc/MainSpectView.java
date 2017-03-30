package lgk.nsbc;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import lgk.nsbc.view.SpectCRUDView;
import lgk.nsbc.view.SpectDataRepresentationView;
import org.springframework.beans.factory.annotation.Autowired;

@Title("Excel upload")
@Theme("valo")
@SpringUI
public class MainSpectView extends UI {
    @Autowired
    private SpectCRUDView spectCRUDView;
    @Autowired
    private SpectDataRepresentationView spectDataRepresentationView;

    @Override
    protected void init(VaadinRequest request) {
        TabSheet tabSheet = new TabSheet();
        tabSheet.addTab(spectCRUDView, "Заполнение");
        tabSheet.addTab(spectDataRepresentationView, "Просмотр");
        setContent(tabSheet);
    }
}
