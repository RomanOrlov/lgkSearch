package lgk.nsbc.view;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.*;
import lgk.nsbc.DataMigrationService;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ParsingView extends Window {
    private VerticalLayout mainLayout = new VerticalLayout();
    private Map<String, Object> selectedPatients = new TreeMap<>();
    private Map<String, Grid> conflicts = new HashMap<>();

    public ParsingView(DataMigrationService dataMigrationService, String message, Map<String, IndexedContainer> data) {
        super("Результаты миграции данных из excel");
        setWidth("600px");
        setHeight("600px");
        setClosable(false);
        setModal(true);
        mainLayout.setSizeFull();
        TextArea parsingText = new TextArea("Информация", message);
        parsingText.setRows(10);
        parsingText.setReadOnly(true);
        parsingText.setSizeFull();
        mainLayout.addComponent(parsingText);
        VerticalLayout grids = new VerticalLayout();
        grids.setSizeFull();
        for (Map.Entry<String, IndexedContainer> entry : data.entrySet()) {
            Grid grid = new Grid(entry.getKey(), entry.getValue());
            grid.setSizeFull();
            grids.addComponent(grid);
            conflicts.put(entry.getKey(), grid);
        }
        mainLayout.addComponent(grids);
        initButtonsPanel(dataMigrationService, data);
        setContent(mainLayout);
        mainLayout.setExpandRatio(parsingText, 0.75f);
        mainLayout.setExpandRatio(grids, 0.5f);
        UI.getCurrent().addWindow(this);
    }

    private void initButtonsPanel(DataMigrationService dataMigrationService, Map<String, IndexedContainer> data) {
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setHeight("40px");
        buttons.setWidth("100%");
        Button accept = new Button("Завершить");
        accept.addClickListener(event -> {
            for (Map.Entry<String, Grid> entry : conflicts.entrySet()) {
                Object selectedRow = entry.getValue().getSelectedRow();
                if (selectedRow == null) continue;
                Item item = data.get(entry.getKey()).getItem(selectedRow);
                Object id = item.getItemProperty("ID").getValue();
                selectedPatients.put(entry.getKey(), id);
            }
            if (data.size() == selectedPatients.size()) {
                close();
                dataMigrationService.addResolvedConflicts(selectedPatients);
                dataMigrationService.saveDataToDB();
            } else {
                Notification.show("Не все конфликты были разрешены Нажмите Отложить, если хотите разрешить конфликты позже.");
            }
        });
        accept.setSizeFull();
        buttons.addComponent(accept);
        if (!data.isEmpty()) {
            Button cancel = new Button("Отложить");
            cancel.addClickListener(event -> close());
            buttons.addComponent(cancel);
            cancel.setSizeFull();
        }
        mainLayout.addComponent(buttons);
    }
}
