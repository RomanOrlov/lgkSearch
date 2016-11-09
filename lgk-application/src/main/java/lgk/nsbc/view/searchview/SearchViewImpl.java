package lgk.nsbc.view.searchview;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.VaadinSessionScope;
import lgk.nsbc.backend.Target;
import lgk.nsbc.backend.samples.Sample;
import lgk.nsbc.backend.search.dbsearch.SelectColumn;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.ViewChangeListener;
import lgk.nsbc.presenter.SearchPresenter;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;
import org.vaadin.dialogs.ConfirmDialog;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static lgk.nsbc.view.Util.getColumnFilter;

@VaadinSessionScope
@SpringComponent
public class SearchViewImpl extends CustomComponent implements SearchView,View {
    private NativeSelect selectTarget = new NativeSelect("Искать:", Arrays.asList(Target.values()));
    private NativeSelect selectSample = new NativeSelect("Текущая выборка:");

    // Настройка критериев и выводимой информации
    private Button setUpCriteria = new Button("Фильтр поиска");
    private Button selectColumns = new Button("Выводимая информация");

    // Инструменты для работы с выборками
    private Button addTargetsToBuffer = new Button("Добавить");
    private Button lookUpSample = new Button("Посмотреть выборку");
    private Button createSample = new Button("Создать выборку");
    private Button deleteSampleData = new Button("Удалить");
    private Button deleteSample = new Button("Удалить выборку");

    // Инструменты для работы с результатом поиска
    private Button resetSelectedTargets = new Button("Сбросить выбранное");
    private Button startSearch = new Button("Поиск");
    private Button exportToExcel = new Button("Экспорт в Excel");
    private Button viewSQLRequest = new Button("SQL");

    private Grid searchResult = new Grid();
    @Autowired
    private SelectColumnsWindow selectColumnsWindow;
    @Autowired
    private CreateNewSampleWindow createNewSampleWindow;
    @Autowired
    private SQLViewWindow lastSQLRequest;
    @Autowired
    private CriteriaViewImpl criteriaViewWindow;
    private SearchPresenter searchPresenter;

    @Autowired
    public SearchViewImpl(SearchPresenter searchPresenter) {
        this.searchPresenter = searchPresenter;
    }

    @PostConstruct
    private void init() {
        setUpCriteria.addClickListener(clickEvent -> getUI().addWindow(criteriaViewWindow));
        selectColumns.addClickListener(clickEvent -> getUI().addWindow(selectColumnsWindow));

        // Подразумевается, что имзенить цель поиска можем только при создании новой выборки
        selectTarget.addValueChangeListener(event -> searchPresenter.changeCurrentSearchTarget((Target) event.getProperty().getValue()));
        selectTarget.setNullSelectionAllowed(false);

        selectSample.setContainerDataSource(searchPresenter.getSamples());
        selectSample.setNullSelectionAllowed(false);
        // Текущее значение зависит от логики в презентере (либо новая выборка, либо сохраненная в сессии)
        selectSample.addValueChangeListener(valueChangeEvent -> searchPresenter.handleSampleChanged((Sample) valueChangeEvent.getProperty().getValue()));
        // Старт поиска
        startSearch.addClickListener(event -> searchPresenter.handleFindButtonClick());
        startSearch.setStyleName("primary");

        addTargetsToBuffer.addClickListener(clickEvent -> searchPresenter.handleAddToSample(searchResult.getSelectedRows()));

        resetSelectedTargets.addClickListener(clickEvent -> searchResult.deselectAll());

        deleteSample.addClickListener(clickEvent -> searchPresenter.handleDeleteCurrentSample());

        lookUpSample.addClickListener(clickEvent -> searchPresenter.handleLookUpSample());

        deleteSampleData.addClickListener(clickEvent -> searchPresenter.handleDeleteSampleData(searchResult.getSelectedRows()));

        createSample.addClickListener(clickEvent -> showNewSampleDialog());

        exportToExcel.addClickListener(clickEvent -> searchPresenter.handleExportToExcel());

        viewSQLRequest.addClickListener(event -> {
            // Check because it's not modal
            if (!getUI().getWindows().contains(lastSQLRequest))
                getUI().addWindow(lastSQLRequest);
        });

        searchResult.setVisible(false);
        searchResult.addFooterRowAt(0).setStyleName("primary");
        searchResult.addHeaderRowAt(0).setStyleName("primary");
        searchResult.setSizeFull();
        searchResult.setColumnReorderingAllowed(true);
        searchResult.setSelectionMode(Grid.SelectionMode.MULTI);
        initLayout();
        refreshDisplayInfo();
        refreshCriteria();
    }

    private void initLayout() {
        GridLayout layout = new GridLayout();
        layout.setSpacing(true);
        layout.setMargin(new MarginInfo(false, false, false, true));
        layout.setSizeFull();
        HorizontalLayout instruments = new HorizontalLayout(selectColumns,
                setUpCriteria,
                startSearch,
                resetSelectedTargets,
                exportToExcel,
                viewSQLRequest);
        HorizontalLayout select = new HorizontalLayout(selectTarget,
                selectSample,
                createSample,
                deleteSample,
                lookUpSample,
                addTargetsToBuffer,
                deleteSampleData
        );
        select.forEach(component -> select.setComponentAlignment(component, Alignment.MIDDLE_CENTER));
        instruments.setSizeUndefined();
        instruments.setSpacing(true);
        select.setSpacing(true);
        layout.addComponents(select, instruments, searchResult);
        setCompositionRoot(layout);
    }

    @Override
    public void showResults(IndexedContainer container) {
        if (container.size() == 0) {
            Notification.show("Пустой запрос, ничего не вернул.");
            return;
        }
        searchResult.setVisible(true);
        searchResult.setEnabled(true);
        searchResult.removeAllColumns();
        searchResult.setContainerDataSource(container);
        Grid.HeaderRow headerRow = searchResult.getHeaderRow(0);
        Object firstColumnId = null;
        for (Grid.Column column : searchResult.getColumns()) {
            column.setHidable(true);
            Object columnId = column.getPropertyId();
            Grid.HeaderCell headerCell = headerRow.getCell(columnId);
            headerCell.setComponent(getColumnFilter(searchResult, columnId));
            headerCell.setStyleName("filter-header");
            if (firstColumnId == null) {
                firstColumnId = columnId;
            }
        }
        Grid.FooterRow footerRow = searchResult.getFooterRow(0);
        footerRow.getCell(firstColumnId).setText("Количество результатов: " + container.size());
    }

    @Override
    public void refreshDisplayInfo() {
        selectColumnsWindow.refreshDisplayInfo(searchPresenter.getDisplayInfo());
    }

    @Override
    public List<SelectColumn> getOrderedSelections() {
        return selectColumnsWindow.getOrderedSelections();
    }

    @Override
    public void refreshCriteria() {
        criteriaViewWindow.refreshCriteriaData(searchPresenter.getAllCriteria());
    }

    @Override
    public void showNewSampleDialog() {
        getUI().addWindow(createNewSampleWindow);
    }

    @Override
    public void showErrorMessage(String text) {
        Notification.show(text);
    }

    @Override
    public void changeCurrentSample(Sample sample) {
        selectSample.setValue(sample);
    }

    @Override
    public void showConfirmDialog(String caption, String message, Consumer<Boolean> booleanConsumer) {
        ConfirmDialog.show(getUI(),
                caption,
                message,
                "Подтвердить",
                "Отмена",
                (ConfirmDialog.Listener) confirmDialog -> booleanConsumer.accept(confirmDialog.isConfirmed()));
    }

    @Override
    public void setSampleComponentsEnable(boolean enable) {
        selectTarget.setEnabled(!enable);
        createSample.setEnabled(!enable);
        deleteSample.setEnabled(enable);
        lookUpSample.setEnabled(enable);
        deleteSampleData.setEnabled(enable);
        searchResult.setEnabled(false);
    }

    @Override
    public void changeSearchTarget(Target target) {
        selectTarget.setValue(target);
        searchResult.setEnabled(false);
    }

    @Override
    public void refreshLastSQLRequest(String sql) {
        lastSQLRequest.refreshSQLRequest(sql);
}

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}