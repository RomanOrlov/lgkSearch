package lgk.nsbc.presenter;

import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.spring.annotation.VaadinSessionScope;
import com.vaadin.ui.Notification;
import lgk.nsbc.backend.SearchManager;
import lgk.nsbc.backend.dao.SamplesRepository;
import lgk.nsbc.backend.dao.SysSessionsRepository;
import lgk.nsbc.backend.entity.SysSessions;
import lgk.nsbc.backend.entity.sample.Sample;
import lgk.nsbc.backend.entity.sample.SampleData;
import lgk.nsbc.backend.info.DisplayedInfo;
import lgk.nsbc.backend.info.SampleAdapter;
import lgk.nsbc.backend.info.SampleAdapterFactory;
import lgk.nsbc.backend.info.criteria.Criteria;
import lgk.nsbc.backend.info.searchable.SearchTarget;
import lgk.nsbc.view.searchview.SearchView;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
@VaadinSessionScope
public class SearchPresenterImpl implements SearchPresenter, Serializable {
    @Autowired
    private SysSessionsRepository sysSessionsRepository;
    @Autowired
    private SamplesRepository samplesRepository;
    @Autowired
    private SearchManager searchManager;
    @Autowired
    private SampleAdapterFactory sampleAdapterFactory;
    private SearchView searchView;
    private SysSessions session;
    // Текущая выборка - по дефолту новая выборка. Но возможно поставить сразу и какую то другую
    private SampleAdapter currentSample;
    // Всегда работаем с выборкой (особенно когда выбрали новую выборку, - она уже существует)
    private SampleAdapter newSample;
    // Выборки пользователя, инициализируем в конструкторе, и собираем.
    private transient BeanItemContainer<SampleAdapter> userSamples;
    // Нужен для экспорта в Excel
    private IndexedContainer lastResult;
    private List<Integer> uniqueIdentifiers;
    // Информация, необходимая для корректного отображения столбцов в таблице
    private List<String> selectFieldsId;

    @PostConstruct
    private void init() {
        newSample = sampleAdapterFactory.getSampleAdapter();
        String lgkSessionId = "9ec2104e97256dc639754ae07b5eb7bf";
        this.session = sysSessionsRepository.findBySid(lgkSessionId);
        // Загружаем сами выборки (а также информацию о выбранных критериях и столбцах)
        List<Sample> userSamples = samplesRepository.findByUserId((session.getSysAgents().getN()));
        List<SampleAdapter> sampleAdapters = userSamples.stream()
                .map(sample -> sampleAdapterFactory.getSampleAdapter().parseSample(sample))
                .collect(toList());
        sampleAdapters.add(0, newSample);
        this.userSamples = new BeanItemContainer<>(SampleAdapter.class, sampleAdapters);
        currentSample = newSample;
    }

    @Override
    public void handleFindButtonClick() {
        // Выбрали только выбранные заданные критерии.
        List<Criteria> criteriaArrayList = currentSample.getOnlySelectedCriteria();
        if (criteriaArrayList.size() == 0) {
            Notification.show("Отсутствуют критерии/Критерии выбраны, но не настроены");
            return;
        }
        // Лепим одно большое условие
        Condition condition = criteriaArrayList.stream()
                .map(Criteria::getCondition)
                .reduce(Condition::and)
                .orElseThrow(RuntimeException::new);
        findFromDataBase(condition);
    }

    /**
     * Вывод содержимого выборки
     */
    @Override
    public void handleLookUpSample() {
        Condition condition = currentSample.getSearchTarget()
                .getUniqueKey()
                .in(currentSample
                        .getSampleData()
                        .stream()
                        .map(SampleData::getObjectUniqueId)
                        .collect(toList()));
        findFromDataBase(condition);
    }

    private void findFromDataBase(Condition condition) {
        SearchTarget searchTarget = currentSample.getSearchTarget();
        // Выбрали только те столбцы, которые были отмечены пользователем (в порядке)
        List<DisplayedInfo> selectedDisplayInfo = new ArrayList<>(searchView.getOrderedSelections());
        // Если не выбраны никакие поля (а я запрещаю SELECT * FROM ) то ничего и не искать
        if (selectedDisplayInfo.size() == 0) {
            Notification.show("Не выбрана цель поиска или не выбрана выводимая информация");
            return;
        }
        List<Criteria> criteriaArrayList = currentSample.getOnlySelectedCriteria();
        // Для передачи в менеджер поиска нужны поля
        List<SelectField<?>> selectFields = selectedDisplayInfo.stream()
                .map(displayedInfo -> (SelectField<?>) displayedInfo.getTableField())
                .collect(toList());
        selectFieldsId = selectedDisplayInfo.stream()
                .map(DisplayedInfo::getRusName)
                .collect(toList());
        // Всегда тянем из базы уникальные номера того, что ищем.
        selectFields.add(0, searchTarget.getUniqueKey());
        selectFieldsId.add(0, "uniqueKey");

        // Собираем все таблицы (и для SELECT и для WHERE) для join
        Map<Table, Condition> joinTablesAndConditions = Stream.concat(criteriaArrayList.stream().map(Criteria::getTable),
                selectedDisplayInfo.stream().map(DisplayedInfo::getTable))
                .distinct()
                .collect(Collectors.toMap(t -> t, searchTarget::getJoinCondition));
        try {
            SearchManager.RequestMetaInfo requestMetaInfo = searchManager.getResult(searchTarget, condition, selectFields, joinTablesAndConditions);
            searchView.refreshLastSQLRequest(requestMetaInfo.getSql());
            searchView.showResults(parseResult(requestMetaInfo.getRecords()));
        } catch (SQLException e) {
            Notification.show("Системная ошибка", "Произошла ошибка, обратитесь к разработчику", Notification.Type.ERROR_MESSAGE);
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<Criteria> getAvailableCriteria() {
        return currentSample.getCriteriaList();
    }

    /**
     * Работает только при смене цели поиска (То есть если работаем с новой выборкой)
     *
     * @param searchTarget новая цель новой выборки
     */
    @Override
    public void changeCurrentSearchTarget(SearchTarget searchTarget) {
        // Обновляем только если цель поиска изменилась и если работаем с новой выборкой
        if (currentSample.getSearchTarget() == searchTarget || currentSample != newSample) {
            return;
        }
        currentSample.changeSearchTarget(searchTarget);
        searchView.refreshDisplayInfo();
    }

    @Override
    public void handleSampleChanged(SampleAdapter sampleAdapter) {
        if (currentSample == sampleAdapter) return;
        // Меняем текущую выборку
        currentSample = sampleAdapter == newSample ? newSample : sampleAdapter;
        refreshView();
    }

    /**
     * Обновляем всё в View
     * Обновляем список выводимых столбцов, критерии, высталяем правильную цель поиска
     * выставляем текущую выборку, и блокируем некоторые компоненты.
     */
    private void refreshView() {
        searchView.refreshDisplayInfo();
        searchView.refreshCriteria();
        searchView.changeSearchTarget(currentSample.getSearchTarget());
        searchView.changeCurrentSample(currentSample);
        searchView.setSampleComponentsEnable(currentSample != newSample);
    }

    // Информация о текущей цели поиска есть в этом классе
    @Override
    public List<DisplayedInfo> getDisplayInfo() {
        return currentSample.getDisplayedInfoList();
    }

    private IndexedContainer parseResult(Result<Record> result) {
        IndexedContainer resultContainer = new IndexedContainer();
        if (result.isEmpty()) return resultContainer;
        // Очищаем результаты предыдущего поиска
        uniqueIdentifiers.clear();

        // Вытаскиваем информацию о типах и создаем проперти в контейнере
        Class<?>[] classes = result.get(0).valuesRow().types();
        int valuesSize = classes.length;
        // Начинаем с одного, т.к. нулевой, - уникальный индекс
        for (int i = 1; i < classes.length; i++) {
            resultContainer.addContainerProperty(selectFieldsId.get(i), classes[i], null);
        }

        // Заполняем контейнер
        for (Record record : result) {
            Object[] values = new Object[valuesSize];
            for (int i = 0; i < valuesSize; i++) {
                values[i] = record.get(i);
            }
            Item newItem = resultContainer.getItem(resultContainer.addItem());
            // Добавляем уникальный ID в список ID результатов
            uniqueIdentifiers.add((Integer) values[0]);
            for (int i = 1; i < valuesSize; i++) {
                newItem.getItemProperty(selectFieldsId.get(i)).setValue(values[i]);
            }
        }
        lastResult = resultContainer;
        return resultContainer;
    }

    @Override
    public void handleAddToSample(Collection selectedRows) {
        if (selectedRows == null || selectedRows.isEmpty()) {
            searchView.showErrorMessage("Ничего не выбрано для добавления!");
            return;
        }
        if (currentSample == newSample) {
            searchView.showNewSampleDialog();
            return;
        }
        List<Integer> uniqueIdentifiersSelected = getSelectedObjectsUniqueIds(selectedRows);
        currentSample.addSelectedSampleData(uniqueIdentifiersSelected);
    }

    /**
     * Вытаскиваем уникальные ID объектов, удаляем их из самого Grid и базы
     *
     * @param selectedRows Порядок выбранных элементов из списка uniqueIdentifiers
     */
    @Override
    public void handleDeleteSampleData(Collection selectedRows) {
        if (selectedRows == null || selectedRows.isEmpty()) {
            searchView.showErrorMessage("Ничего не выбрано для удаления из выборки!");
            return;
        }
        if (currentSample != newSample) {
            List<Integer> uniqueIdentifiersSelected = getSelectedObjectsUniqueIds(selectedRows);
            currentSample.deleteSelectedSampleData(uniqueIdentifiersSelected);
            selectedRows.forEach(lastResult::removeItem);
        }
    }

    private List<Integer> getSelectedObjectsUniqueIds(Collection selectedRows) {
        return ((Collection<Integer>) selectedRows).stream().map(uniqueIdentifiers::get).collect(toList());
    }

    /**
     * Раз уж мы сюда попали, значит текущая выборка выставлена как новая, всё что осталось сделать
     * для её создания, - дать имя и комментарий, и запостить в базу.
     *
     * @param name    Имя новой выборки
     * @param comment Комментарий к выборке
     */
    @Override
    public void createNewSample(String name, String comment) {
        currentSample.saveSample(name, comment, session.getSysAgents().getN());
        // Заново создаем шаблонную выборку, готовую к работе
        newSample = sampleAdapterFactory.getSampleAdapter();
        userSamples.addItemAt(0, newSample);
        // Переходим в режим работы с выборкой (SelectTarget остался нужным)
        searchView.setSampleComponentsEnable(true);
    }

    @Override
    public BeanItemContainer<SampleAdapter> getSamples() {
        return userSamples;
    }

    @Override
    public void handleDeleteCurrentSample() {
        if (currentSample != newSample) {
            searchView.showConfirmDialog("Удалить выборку " + currentSample,
                    "Будет удалена выборка и всё её содержимое",
                    this::deleteSample);
        } else {
            searchView.showErrorMessage("Не выбрана выборка");
        }
    }

    /**
     * Экспорт в Excel требует наличия Table. У меня же используется Grid (И от него
     * отказаться проблематично, поскольку Table не поддерживает фильтры)
     * Самому собрать Table в принципе не проблема. Пусть пока будет простенький вариант.
     */
    @Override
    public void handleExportToExcel() {
        if (lastResult != null) {
            com.vaadin.ui.Table table = new com.vaadin.ui.Table("Результат поиска", lastResult);
            ExcelExport excelExport = new ExcelExport(table, "Экспортированные данные", "Выгрузка", session.getSysAgents().getName() + ".xls");
            excelExport.export();
        } else {
            searchView.showErrorMessage("Нет результатов поиска");
        }
    }

    @Override
    public void acceptCriteriaChange(List<Criteria> criteria) {
        if (currentSample != newSample) {
            currentSample.refreshCriteria(criteria);
        }
    }

    @Override
    public void acceptDisplayedInfoChange(List<DisplayedInfo> displayedInfo) {
        if (currentSample != newSample) {
            currentSample.refreshDisplayedInfo(displayedInfo);
        }
    }

    private void deleteSample(Boolean isConfirmed) {
        if (isConfirmed && currentSample != newSample) {
            currentSample.removeSample();
            userSamples.removeItem(currentSample);
            currentSample = newSample;
            refreshView();
        }
    }

    public void setSearchView(SearchView searchView) {
        this.searchView = searchView;
    }
}