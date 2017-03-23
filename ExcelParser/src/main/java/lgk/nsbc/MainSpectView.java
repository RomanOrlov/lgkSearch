package lgk.nsbc;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import lgk.nsbc.template.dao.*;
import lgk.nsbc.template.model.NbcPatients;
import lgk.nsbc.util.*;
import lgk.nsbc.view.SpectCRUDView;
import lgk.nsbc.view.SpectDataRepresentationView;
import lgk.nsbc.view.spectflup.AddSpectFlup;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import static lgk.nsbc.template.model.spect.ContourType.ISOLYNE10;
import static lgk.nsbc.template.model.spect.ContourType.ISOLYNE25;

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
