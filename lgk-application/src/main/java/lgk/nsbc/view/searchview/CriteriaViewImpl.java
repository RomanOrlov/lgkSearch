package lgk.nsbc.view.searchview;

import lgk.nsbc.backend.search.dbsearch.Criteria;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import lgk.nsbc.view.TwinTablesSelect;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Класс, реализующий логику настроек критериев
 * Created by Роман on 03.05.2016.
 */
public class CriteriaViewImpl implements CriteriaView {
    private UI ui;
    private Window window = new Window("Настройка критериев");
    private TwinTablesSelect<Criteria> twinTablesSelect;
    // Маленькая форма, для возможности изменения параметров критериев поиска
    private FormLayout formLayout = new FormLayout();
    private VerticalLayout layout = new VerticalLayout();
    private Button acceptButton = new Button("Принять");
    private Button cancelButton = new Button("Отмена");

    public CriteriaViewImpl(UI ui, List<Criteria> criteria, Consumer<List<Criteria>> listConsumer) {
        this.ui = ui;
        twinTablesSelect = new TwinTablesSelect<>(new BeanItemContainer<>(Criteria.class, criteria),
                new BeanItemContainer<>(Criteria.class),
                listConsumer);
        twinTablesSelect.addSelectionsValueChangeListener(valueChangeEvent -> {
            Set selected = (Set) valueChangeEvent.getProperty().getValue();
            if (selected.size() == 1) {
                ((Criteria) selected.iterator().next()).viewCriteriaEditField(formLayout);
            }
        });
        twinTablesSelect.setCaption("Выберите критерии");
        twinTablesSelect.setCaptionAlignment(Alignment.MIDDLE_LEFT);

        window.addCloseListener(closeEvent -> twinTablesSelect.discardChanges());

        acceptButton.addClickListener(event -> {
            twinTablesSelect.acceptChanges();
            window.close();
        });

        cancelButton.addClickListener(event -> {
            twinTablesSelect.discardChanges();
            window.close();
        });
        initLayout();
    }

    private void initLayout() {
        HorizontalLayout buttons = new HorizontalLayout(acceptButton, cancelButton);
        buttons.setComponentAlignment(acceptButton, Alignment.MIDDLE_CENTER);
        buttons.setComponentAlignment(cancelButton, Alignment.MIDDLE_CENTER);
        acceptButton.setStyleName("primary");
        buttons.setSpacing(true);
        layout.addComponents(twinTablesSelect, formLayout, buttons);
        layout.setExpandRatio(twinTablesSelect, 0);
        layout.setExpandRatio(formLayout, 0);
        layout.setExpandRatio(buttons, 0);
        layout.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
        layout.setSizeFull();
        layout.setMargin(new MarginInfo(true, true, false, true));
        window.setHeight("700px");
        window.setWidth("75%");
        window.setContent(layout);
        window.center();
        window.setModal(true);
        window.setResizable(true);
        buttons.setSizeUndefined();
    }

    @Override
    public void setUpCriteria() {
        ui.addWindow(window);
    }

    @Override
    public void refreshCriteriaData(List<Criteria> criteriaList) {
        twinTablesSelect.refreshData(criteriaList);
    }
}