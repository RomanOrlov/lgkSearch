package lgk.nsbc;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import lgk.nsbc.template.dao.*;
import lgk.nsbc.template.model.NbcPatients;
import lgk.nsbc.view.AddSpectFlup;
import lgk.nsbc.view.SpectData;
import lgk.nsbc.view.SuggestionCombobox;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

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
    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    private SpectData spectData;

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setSpacing(true);
        verticalLayout.setMargin(true);

        Upload upload = new Upload("Upload file", new ExcelFileReceiver());
        upload.addFinishedListener((Upload.FinishedListener) event -> {
            dataMigrationService.findPatients(tempFile);
        });

        Label label = new Label("");
        SuggestionCombobox<NbcPatients> combobox = new SuggestionCombobox<>(nbcPatientsDao::getPatientsWithSurnameLike, NbcPatients.class);
        combobox.addValueChangeListener(valueChangeEvent -> {
            NbcPatients selectedPatient = combobox.getSelectedPatient();
            label.setValue(selectedPatient.toString());
        });

        Button newRecord = new Button("Добавить");
        Button readRecords = new Button("Просмотр");
        Button editExistingRecord = new Button("Редактировать");
        Button deleteRecord = new Button("Удалить");
        newRecord.addClickListener(clickEvent -> {
            AddSpectFlup addSpectFlup = beanFactory.getBean(AddSpectFlup.class, combobox.getSelectedPatient());
            UI.getCurrent().addWindow(addSpectFlup);
        });
        readRecords.addClickListener(clickEvent -> {
            spectData.readData(combobox.getSelectedPatient());
        });
        editExistingRecord.addClickListener(clickEvent -> {
            // Берем строчку из грида и вставляем данные в окно
            AddSpectFlup addSpectFlup = beanFactory.getBean(AddSpectFlup.class, combobox.getSelectedPatient());
            UI.getCurrent().addWindow(addSpectFlup);
        });
        deleteRecord.addClickListener(clickEvent -> {
            spectData.deleteSelectedRecords();
        });
        HorizontalLayout crudButtons = new HorizontalLayout(newRecord, readRecords, editExistingRecord, deleteRecord);
        crudButtons.setSpacing(true);
        crudButtons.setWidth("100%");

        TwinColSelect twinColSelect = new TwinColSelect("Фильтры столбцов", spectData.getFilters());
        twinColSelect.setLeftColumnCaption("Отображаемые столбцы");
        twinColSelect.setRightColumnCaption("Скрытые столбцы");
        twinColSelect.addValueChangeListener(valueChangeEvent -> {
            Set value = (Set) valueChangeEvent.getProperty().getValue();
            spectData.updateVisibility(value);
        });
        twinColSelect.setHeight("200px");

        verticalLayout.addComponents(upload, combobox, label, crudButtons, twinColSelect, spectData);
        verticalLayout.setExpandRatio(spectData, 1.0f);
        setContent(verticalLayout);
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
