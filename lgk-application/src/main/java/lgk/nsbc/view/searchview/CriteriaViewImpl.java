package lgk.nsbc.view.searchview;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.VaadinSessionScope;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import lgk.nsbc.backend.info.criteria.Criteria;
import lgk.nsbc.presenter.SearchPresenter;
import lgk.nsbc.view.TwinTablesSelect;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Класс, реализующий логику настроек критериев
 * Created by Роман on 03.05.2016.
 */
@VaadinSessionScope
@SpringView
public class CriteriaViewImpl extends Window implements View {
    private SearchPresenter searchPresenter;
    private TwinTablesSelect<Criteria> twinTablesSelect;
    // Маленькая форма, для возможности изменения параметров критериев поиска
    private FormLayout formLayout = new FormLayout();
    private VerticalLayout layout = new VerticalLayout();
    private Button acceptButton = new Button("Принять");
    private Button cancelButton = new Button("Отмена");

    @Autowired
    public CriteriaViewImpl(SearchPresenter searchPresenter) {
        super("Настройка критериев");
        this.searchPresenter = searchPresenter;
    }

    @PostConstruct
    private void init() {
        new BeanItemContainer<>(Criteria.class);
        twinTablesSelect = new TwinTablesSelect<>(Criteria.class,
                searchPresenter.getAvailableCriteria(),
                new ArrayList<>(),
                searchPresenter::acceptCriteriaChange);
        twinTablesSelect.addSelectionsValueChangeListener(valueChangeEvent -> {
            Set selected = (Set) valueChangeEvent.getProperty().getValue();
            if (selected.size() == 1) {
                ((Criteria) selected.iterator().next()).viewCriteriaEditField(formLayout);
            }
        });
        twinTablesSelect.setCaption("Выберите критерии");
        twinTablesSelect.setCaptionAlignment(Alignment.MIDDLE_LEFT);

        addCloseListener(closeEvent -> twinTablesSelect.discardChanges());

        acceptButton.addClickListener(event -> {
            twinTablesSelect.acceptChanges();
            close();
        });

        cancelButton.addClickListener(event -> {
            twinTablesSelect.discardChanges();
            close();
        });
        initLayout();
    }

    private void initLayout() {
        HorizontalLayout buttons = new HorizontalLayout(acceptButton, cancelButton);
        buttons.setComponentAlignment(acceptButton, Alignment.MIDDLE_CENTER);
        buttons.setComponentAlignment(cancelButton, Alignment.MIDDLE_CENTER);
        buttons.setSizeUndefined();
        buttons.setSpacing(true);
        acceptButton.setStyleName("primary");
        layout.addComponents(twinTablesSelect, formLayout, buttons);
        layout.setExpandRatio(twinTablesSelect, 0);
        layout.setExpandRatio(formLayout, 0);
        layout.setExpandRatio(buttons, 0);
        layout.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
        layout.setSizeFull();
        layout.setMargin(new MarginInfo(true, true, false, true));
        setHeight("700px");
        setWidth("75%");
        setContent(layout);
        center();
        setModal(true);
        setResizable(true);
    }

    public void refreshCriteriaData(List<Criteria> criteriaList) {
        twinTablesSelect.refreshData(criteriaList);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        getUI().addWindow(this);
    }
}