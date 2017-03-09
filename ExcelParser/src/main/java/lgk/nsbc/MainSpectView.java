package lgk.nsbc;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import lgk.nsbc.template.dao.*;
import lgk.nsbc.template.model.BasPeople;
import lgk.nsbc.template.model.NbcPatients;
import lgk.nsbc.template.model.NbcProc;
import lgk.nsbc.template.model.NbcStud;
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
        verticalLayout.addComponent(upload);
        verticalLayout.addComponent(combobox);
        setContent(verticalLayout);
        NbcStud build = NbcStud.builder()
                .nbc_patients_n(1001L)
                .study_type(11L)
                .studydatetime(new Date())
                .build();
        nbcStudDao.createNbcStud(build);
        System.out.println(build.getN());
        System.out.println("WOW");
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
