package lgk.nsbc.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.VaadinSessionScope;
import com.vaadin.ui.*;
import lgk.nsbc.dao.NbcPatientsDao;
import lgk.nsbc.util.*;
import lgk.nsbc.util.excel.ExcelExporter;
import lgk.nsbc.view.spectcrud.HidingGridColumsSelect;
import lgk.nsbc.view.spectcrud.SpectData;
import lgk.nsbc.view.spectcrud.SuggestionCombobox;
import lgk.nsbc.view.spectflup.AddSpectFlup;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import static lgk.nsbc.model.spect.ContourType.ISOLYNE10;
import static lgk.nsbc.model.spect.ContourType.ISOLYNE25;

@Service
@VaadinSessionScope
public class SpectCRUDView extends VerticalLayout implements View{
    // Файл куда заливается Excel file
    private File tempFile;
    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    private DataMigrationService dataMigrationService;
    @Autowired
    private NbcPatientsDao nbcPatientsDao;
    @Autowired
    private SpectData spectData;

    private SuggestionCombobox combobox;

    @PostConstruct
    private void init() {
        setSizeFull();

        Upload upload = new Upload("Upload file", new ExcelFileReceiver());
        upload.addFinishedListener((Upload.FinishedListener) event -> dataMigrationService.findPatients(tempFile));

        Label patientName = new Label();
        combobox = new SuggestionCombobox(nbcPatientsDao::getPatientsWithDifferetNames);
        combobox.addValueChangeListener(valueChangeEvent -> patientName.setValue(combobox.getValue().toString()));

        Button newRecord = new Button("Добавить");
        Button readRecords = new Button("Просмотр");
        Button editExistingRecord = new Button("Редактировать");
        Button deleteRecord = new Button("Удалить");
        Button exportToExcel = new ExcelExporter(spectData, "Excel");
        newRecord.addClickListener(clickEvent -> {
            if (!combobox.getSelectedItem().isPresent()) {
                Notification.show("Не выбран паицент");
                return;
            }
            AddSpectFlup addSpectFlup = beanFactory.getBean(AddSpectFlup.class, combobox.getSelectedItem().get(), spectData);
            UI.getCurrent().addWindow(addSpectFlup);
        });
        readRecords.addClickListener(clickEvent -> readData());
        editExistingRecord.addClickListener(clickEvent -> {
            if (!combobox.getSelectedItem().isPresent()) {
                Notification.show("Не выбран паицент");
                return;
            }
            // Редактирование основано на удалении данных и их вставке.
            if (spectData.asSingleSelect().isEmpty()) {
                Notification.show("Ничего не выбрано для редактирования");
                return;
            }
            Long selectedRowId = spectData.getSelectedRowId();
            AddSpectFlup addSpectFlup = beanFactory.getBean(AddSpectFlup.class, combobox.getSelectedItem().get(), spectData, selectedRowId);
            UI.getCurrent().addWindow(addSpectFlup);
        });
        deleteRecord.addClickListener(clickEvent -> {
            if (spectData.asSingleSelect().isEmpty()) {
                Notification.show("Ничего не выбрано для удаления");
                return;
            }
            spectData.deleteSelectedRecords();
        });

        TwinColSelect twinColSelect = new HidingGridColumsSelect(spectData);
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSizeFull();
        buttons.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        buttons.addComponents(newRecord, readRecords, editExistingRecord, deleteRecord, exportToExcel);

        HorizontalLayout instruments = new HorizontalLayout(twinColSelect, buttons);
        instruments.setWidth("100%");
        instruments.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);

        addComponents(upload, combobox, patientName, instruments, spectData);
        setExpandRatio(spectData, 1.0f);

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
        spectData.readData(combobox.getValue());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

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
