package lgk.nsbc;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import lgk.nsbc.template.dao.BasPeopleDao;
import lgk.nsbc.template.dao.NbcPatientsDao;
import lgk.nsbc.template.dao.NbcProcDao;
import lgk.nsbc.template.dao.NbcTargetDao;
import lgk.nsbc.template.model.BasPeople;
import lgk.nsbc.template.model.NbcPatients;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
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

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        Upload upload = new Upload("Upload file", new ExcelFileReceiver());
        upload.addFinishedListener((Upload.FinishedListener) event -> {
            dataMigrationService.findPatients(tempFile);
        });
        //Suggection suggestionBox = new SuggestionBox(null, null);
        verticalLayout.addComponent(upload);
        //verticalLayout.addComponent(suggestionBox);
        setContent(verticalLayout);
        Set<String> names = new TreeSet<>();
        names.add("Курников");
        names.add("Гаврилина");
        names.add("Тхазеплов");
        names.add("Орлова");
        names.add("Будченко");
        names.add("Янышев");
        names.add("Тихонов");
        names.add("Бирюков");
        names.add("Огонькова");
        List<BasPeople> peoplesBySurname = basPeopleDao.getPeoplesBySurname(names);
        peoplesBySurname.forEach(System.out::println);
        List<NbcPatients> collect = peoplesBySurname.stream()
                .map(basPeople -> nbcPatientsDao.getPatientByBasPeople(basPeople))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        collect.forEach(nbcPatients -> {
            Long aLong1 = nbcProcDao.countProceduresForPatient(nbcPatients);
            Long aLong = nbcTargetDao.countTargetsForPatient(nbcPatients);
            System.out.println(nbcPatients.toString() + " " + aLong1 + " " + aLong);
        });
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
