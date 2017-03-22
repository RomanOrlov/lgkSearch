package lgk.nsbc;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import lgk.nsbc.template.dao.*;
import lgk.nsbc.template.model.NbcPatients;
import lgk.nsbc.util.SuggestionCombobox;
import lgk.nsbc.view.SpectData;
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
    @Autowired
    private PatientsDuplicatesResolver patientsDuplicatesResolver;

    private SuggestionCombobox<NbcPatients> combobox;

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

        Label patientName = new Label();
        combobox = new SuggestionCombobox<>(NbcPatients.class,
                nbcPatientsDao::getPatientsWithDifferetNames,
                patientsDuplicatesResolver::getPatient);
        combobox.addValueChangeListener(valueChangeEvent -> {
            NbcPatients selectedPatient = combobox.getSelectedPatient();
            patientName.setValue(selectedPatient.toString());
        });

        Button newRecord = new Button("Добавить");
        Button readRecords = new Button("Просмотр");
        Button editExistingRecord = new Button("Редактировать");
        Button deleteRecord = new Button("Удалить");
        Button exportToExcel = new Button("Excel");
        newRecord.addClickListener(clickEvent -> {
            if (combobox.getSelectedPatient() == null) {
                Notification.show("Не выбран паицент");
                return;
            }
            AddSpectFlup addSpectFlup = beanFactory.getBean(AddSpectFlup.class, combobox.getSelectedPatient(), spectData);
            UI.getCurrent().addWindow(addSpectFlup);
        });
        readRecords.addClickListener(clickEvent -> readData());
        editExistingRecord.addClickListener(clickEvent -> {
            if (combobox.getSelectedPatient() == null) {
                Notification.show("Не выбран паицент");
                return;
            }
            // Редактирование основано на удалении данных и их вставке.
            if (spectData.getSelectedRows().isEmpty()) {
                Notification.show("Ничего не выбрано для редактирования");
                return;
            }
            if (spectData.getSelectedRows().size() != 1) {
                Notification.show("Для редактирования выбрано более 1 записи");
                return;
            }
            Long selectedRowId = spectData.getSelectedRowId();
            AddSpectFlup addSpectFlup = beanFactory.getBean(AddSpectFlup.class, combobox.getSelectedPatient(), spectData, selectedRowId);
            UI.getCurrent().addWindow(addSpectFlup);
        });
        deleteRecord.addClickListener(clickEvent -> {
            if (spectData.getSelectedRows().isEmpty()) {
                Notification.show("Ничего не выбрано для удаления");
                return;
            }
            spectData.deleteSelectedRecords();
        });

        TwinColSelect twinColSelect = new TwinColSelect();
        IndexedContainer indexedContainer = new IndexedContainer(spectData.getFilters());
        twinColSelect.setContainerDataSource(indexedContainer);
        twinColSelect.setLeftColumnCaption("Отображаемые столбцы");
        twinColSelect.setRightColumnCaption("Скрытые столбцы");
        twinColSelect.addValueChangeListener(valueChangeEvent -> {
            Set value = (Set) valueChangeEvent.getProperty().getValue();
            spectData.updateVisibility(value);
        });
        twinColSelect.setWidth("100%");
        twinColSelect.setHeight("100px");

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSizeFull();
        buttons.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        buttons.addComponents(newRecord, readRecords, editExistingRecord, deleteRecord, exportToExcel);

        HorizontalLayout instruments = new HorizontalLayout(twinColSelect, buttons);
        instruments.setSpacing(true);
        instruments.setWidth("100%");
        instruments.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);

        verticalLayout.addComponents(upload, combobox, patientName, instruments, spectData);
        verticalLayout.setExpandRatio(spectData, 1.0f);
        setContent(verticalLayout);

        // По дефолту что то будет скрыто (Например изолинии, которые не нужны)
        Set<String> invisibleColumns = new HashSet<>();
        invisibleColumns.add(ISOLYNE10.getName());
        invisibleColumns.add(ISOLYNE25.getName());
        twinColSelect.setValue(invisibleColumns);
        combobox.focus();
    }

    private void readData() {
        if (combobox.isEmpty()) {
            Notification.show("Не выбран паицент");
            return;
        }
        spectData.readData(combobox.getSelectedPatient());
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