package lgk.nsbc;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import lgk.nsbc.template.dao.*;
import lgk.nsbc.template.model.*;
import lgk.nsbc.view.AddSpectFlup;
import lgk.nsbc.view.SuggestionCombobox;
import lgk.nsbc.view.SuggestionContainer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

@Title("Excel upload")
@Theme("valo")
@SpringUI
public class MainSpectView extends UI {
    @Autowired
    private DataMigrationService dataMigrationService;
    protected File tempFile;
    @Autowired
    private BasPeopleDao basPeopleDao;
    @Autowired
    private NbcPatientsDao nbcPatientsDao;
    @Autowired
    private NbcTargetDao nbcTargetDao;
    @Autowired
    private NbcProcDao nbcProcDao;
    @Autowired
    private NbcStudDao nbcStudDao;

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        Upload upload = new Upload("Upload file", new ExcelFileReceiver());
        upload.addFinishedListener((Upload.FinishedListener) event -> {
            dataMigrationService.findPatients(tempFile);
        });
        SuggestionCombobox<NbcPatients> combobox = new SuggestionCombobox<>(nbcPatientsDao::getPatientsWithSurnameLike,NbcPatients.class);
        combobox.setWidth("100%");
        verticalLayout.addComponents(upload, combobox);
        Button c = new Button("Добавить новую щапись");
        Button button = new Button("Добавить новую щапись");
        setContent(verticalLayout);
        NbcPatients patients = nbcPatientsDao.getPatientsWithSurnameLike("Голубых").get(0);
        List<NbcTarget> patientsTargets = nbcTargetDao.getPatientsTargets(patients);
        addWindow(new AddSpectFlup(patientsTargets, patients));
    }

    private class ExcelFileReceiver implements Upload.Receiver {
        @Override
        public OutputStream receiveUpload(String filename, String mimeType) {
            if (!filename.endsWith(".xlsx")) throw new RuntimeException("Это должен быть файл с форматом .xlsx");
            try {
                tempFile = File.createTempFile("temp", ".xlsx");
                return new FileOutputStream(tempFile);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
