package lgk.nsbc.spect.view.spectcrud;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.VaadinSessionScope;
import com.vaadin.ui.*;
import lgk.nsbc.model.spect.ContourType;
import lgk.nsbc.spect.model.SpectDataManager;
import lgk.nsbc.spect.util.DataMigrationService;
import lgk.nsbc.spect.util.components.NavigationBar;
import lgk.nsbc.spect.util.excel.SpectDataExcelExporter;
import lgk.nsbc.spect.util.components.SuggestionCombobox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.dialogs.ConfirmDialog;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import static lgk.nsbc.model.spect.ContourType.*;

@Service
@VaadinSessionScope
public class SpectCRUDView extends VerticalLayout implements View {
    // Файл куда заливается Excel file
    private File tempFile;
    @Autowired
    private DataMigrationService dataMigrationService;
    @Autowired
    private SpectGrid spectGrid;
    @Autowired
    private SpectDataManager spectDataManager;
    @Autowired
    private SuggestionCombobox combobox;

    private Button readAllRecords;
    private NavigationBar navigationBar = new NavigationBar();

    @PostConstruct
    private void init() {
        setSizeFull();
        setSpacing(false);

        Upload upload = new Upload("Upload file", new ExcelFileReceiver());
        upload.addFinishedListener((Upload.FinishedListener) event -> dataMigrationService.findPatients(tempFile));

        Button newRecord = new Button("Добавить");
        readAllRecords = new Button("Все записи");
        Button deleteRecord = new Button("Удалить");
        HorizontalLayout exportToExcel = new SpectDataExcelExporter(spectGrid, "Экспорт в Excel");
        newRecord.addClickListener(clickEvent -> {
            if (!combobox.getSelectedItem().isPresent()) {
                Notification.show("Не выбран пациент");
                return;
            }
            spectGrid.addNewSpecDataRecord(combobox.getValue());
        });
        readAllRecords.addClickListener(event -> spectGrid.refreshAllData());
        deleteRecord.addClickListener(clickEvent -> {
            if (spectGrid.asSingleSelect().isEmpty()) {
                Notification.show("Ничего не выбрано для удаления");
                return;
            }
            ConfirmDialog.show(getUI(), "Удалить выбраную запись", "Вы уверены?", "Да", "Нет", dialog -> {
                if (dialog.isConfirmed()) {
                    spectDataManager.deleteSpectData(spectGrid.asSingleSelect().getValue());
                    spectGrid.deleteSelected();
                }
            });
        });

        HidingGridColumsSelect twinColSelect = new HidingGridColumsSelect(spectGrid);
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSizeFull();
        buttons.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        buttons.addComponents(newRecord, deleteRecord, readAllRecords, exportToExcel, upload);

        HorizontalLayout instruments = new HorizontalLayout(twinColSelect, buttons);
        instruments.setExpandRatio(buttons, 1);
        instruments.setWidth("100%");
        instruments.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);

        addComponents(navigationBar, combobox, instruments, spectGrid);
        setExpandRatio(spectGrid, 1.0f);

        // По дефолту что то будет скрыто (Например изолинии, которые не нужны)
        Set<String> invisibleColumns = new HashSet<>(ContourType.getNames());
        invisibleColumns.remove(SPHERE.getName());
        twinColSelect.setValue(invisibleColumns);
        combobox.focus();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        readAllRecords.click();
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
