package lgk.nsbc.presenter;

import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.spring.annotation.VaadinSessionScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import lgk.nsbc.backend.*;
import lgk.nsbc.backend.dao.SysSessionsRepository;
import lgk.nsbc.backend.entity.Session;
import lgk.nsbc.backend.entity.SysSessions;
import lgk.nsbc.backend.samples.Sample;
import lgk.nsbc.backend.samples.SampleData;
import lgk.nsbc.backend.search.dbsearch.Criteria;
import lgk.nsbc.backend.search.dbsearch.SelectColumn;
import lgk.nsbc.view.searchview.SearchView;
import org.jooq.*;
import org.jooq.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@VaadinSessionScope
@Service
public class SearchPresenterImpl implements SearchPresenter {
    @Autowired
    private SysSessionsRepository sysSessionsRepository;
    @Autowired
    private SelectColumnManager selectColumnManager;
    @Autowired
    private DBStructureInfo structureInfo;
    @Autowired
    private SearchManager searchManager;
    @Autowired
    private SamplesManager samplesManager;
    @Autowired
    private SearchView searchView;

    private SysSessions session;

    // Текущая выборка - по дефолту новая выборка. Но возможно поставить сразу и какую то другую
    private Sample currentSample;
    // Всегда работаем с выборкой (особенно когда выбрали новую выборку, - она уже существует)
    private Sample newSample;

    // Выборки пользователя, инициализируем в конструкторе, и собираем.
    private BeanItemContainer<Sample> userSamples;

    // Нужен для экспорта в Excel
    private IndexedContainer lastResult;
    private ArrayList<Long> uniqueIdentifiers = new ArrayList<>();

    // Информация, необходимая для корректного отображения столбцов в таблице
    private ArrayList<String> selectFieldsId = new ArrayList<>();

    public SearchPresenterImpl() {
    }

    @PostConstruct
    private void init() {
        newSample = getNewTemplateSample();
        String lgkSessionId = "9ec2104e97256dc639754ae07b5eb7bf";
        this.session = sysSessionsRepository.findBySid(lgkSessionId);
        try {
            // Загружаем сами выборки (а также информацию о выбранных критериях и столбцах)
            userSamples = new BeanItemContainer<>(Sample.class, samplesManager.getSamples(session.getSysAgents().getN()));
            // Для каждой выборки восстаналиваем о выбранных критериях/столбцах
            selectColumnManager.parseSamples(userSamples.getItemIds());
            userSamples.addItemAt(0, newSample);
            currentSample = newSample;
        } catch (SQLException e) {
            Notification.show("SQL Ошибка во время загрузки выборок пользователя");
            Logger.getGlobal().log(Level.SEVERE, "", e);
        }
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
        Iterator<Criteria> criteriaListIterator = criteriaArrayList.iterator();
        Condition condition = criteriaListIterator.next().getCondition();
        while (criteriaListIterator.hasNext()) {
            condition = condition.and(criteriaListIterator.next().getCondition());
        }
        findFromDataBase(condition);
    }

    @Override
    public void handleLookUpSample() {
        Condition condition = currentSample
                .getSearchTarget()
                .getUniqueKey()
                .in(currentSample
                        .getSampleData()
                        .stream()
                        .map(SampleData::getUnique_id)
                        .collect(Collectors.toList()));
        findFromDataBase(condition);
    }

    private void findFromDataBase(Condition condition) {
        Target target = currentSample.getSearchTarget();
        // Выбрали только те столбцы, которые были отмечены пользователем
        ArrayList<SelectColumn> selectedDisplayInfo = new ArrayList<>(searchView.getOrderedSelections());
        // Если не выбраны никакие поля (а я запрещаю SELECT * FROM ) то ничего и не искать
        if (target == null || selectedDisplayInfo.size() == 0) {
            Notification.show("Не выбрана цель поиска или не выбрана выводимая информация");
            return;
        }

        // Выбрали только выбранные заданные критерии.
        List<Criteria> criteriaArrayList = currentSample.getOnlySelectedCriteria();

        // Для передачи в менеджер поиска нужны поля
        List<SelectField<?>> selectFields = new ArrayList<>(selectedDisplayInfo.size());
        selectFieldsId = new ArrayList<>();
        for (SelectColumn displayInfo : selectedDisplayInfo) {
            selectFields.add(displayInfo.getTableField());
            selectFieldsId.add(displayInfo.toString());
        }

        // Всегда тянем из базы уникальные номера того, что ищем.
        selectFields.add(0, target.getUniqueKey());
        selectFieldsId.add(0, "Unique_Key");

        // Собираем все таблицы (и для SELECT и для WHERE) для join
        Set<Table> tables = criteriaArrayList.stream()
                .map(criteria -> criteria.getTableField().getTable())
                .collect(Collectors.toSet());
        tables.addAll(
                selectedDisplayInfo.stream()
                        .map(SelectColumn::getTable)
                        .collect(Collectors.toSet())
        );

        // Вроде как всегда должна быть перестрахуемся костылями
        if (tables.contains(target.getLgkTable())) {
            tables.remove(target.getLgkTable());
        }
        // Тут собираем таблицы для join и необходимые условия
        IdentityHashMap<Table, Condition> joinTablesAndConditions = new IdentityHashMap<>();
        tables.forEach(table -> joinTablesAndConditions.put(table, structureInfo.getJoinCondition(target, table)));
        try {
            SearchManager.RequestMetaInfo requestMetaInfo = searchManager.getResult(target, condition, selectFields, joinTablesAndConditions);
            searchView.refreshLastSQLRequest(requestMetaInfo.getSql());
            searchView.showResults(parseResult(requestMetaInfo.getRecords()));
        } catch (SQLException e) {
            Notification.show("Системная ошибка", "Произошла ошибка, обратитесь к разработчику", Notification.Type.ERROR_MESSAGE);
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<Criteria> getAllCriteria() {
        return currentSample.getAllCriteria();
    }

    /**
     * Работает только при смене цели поиска (То есть если работаем с новой выборкой)
     *
     * @param searchTarget новая цель новой выборки
     */
    @Override
    public void changeCurrentSearchTarget(Target searchTarget) {
        // Обновляем только если цель поиска изменилась и если работаем с новой выборкой
        if (currentSample.getSearchTarget() == searchTarget || currentSample != newSample) {
            return;
        }
        currentSample.setSearchTarget(searchTarget);
        currentSample.setAllSelectColumns(selectColumnManager.getViewForSearch(searchTarget));
            /* Смена цели поииска не должна отразится на текущих критериях
             Обновим список доступных отображений толбцов на странице поиска */
        searchView.refreshDisplayInfo();
    }

    @Override
    public void handleSampleChanged(Sample sample) {
        if (currentSample == sample) return;
        // Меняем текущую выборку
        currentSample = sample == newSample ? newSample : sample;
        refreshView();
    }

    /**
     * Обновляем всё в View
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
    public List<SelectColumn> getDisplayInfo() {
        return currentSample.getAllSelectColumns();
    }

    private IndexedContainer parseResult(Result<Record> result) {
        IndexedContainer resultContainer = new IndexedContainer();
        if (result.isEmpty()) return resultContainer;
        // Очищаем результаты предыдущего поиска
        uniqueIdentifiers = new ArrayList<>();

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
            uniqueIdentifiers.add((Long) values[0]);
            for (int i = 1; i < valuesSize; i++) {
                newItem.getItemProperty(selectFieldsId.get(i)).setValue(values[i]);
            }
        }
        lastResult = resultContainer;
        return resultContainer;
    }

    @Override
    public void handleAddToSample(Collection collection) {
        if (collection == null || collection.isEmpty()) {
            searchView.showErrorMessage("Ничего не выбрано для добавления!");
            return;
        }
        if (currentSample == newSample) {
            // Если выборка новая, значит надо вернутся и создать её
            searchView.showNewSampleDialog();
            return;
        }
        // Добавляем в выборку данные, но сначала конвертируем в коллекцию из Long
        ArrayList<SampleData> samples = new ArrayList<>(collection.size());
        // o - всего лишь порядок в lastResult
        for (Integer o : (Collection<Integer>) collection) {
            // Не забываем, что selectFieldsId.get(0) - уникальный проперти UniqueId, невидимый в результатах
            Long uniqueId = uniqueIdentifiers.get(o);
            SampleData sampleData = new SampleData(currentSample.getN(), uniqueId);
            // Проверяем, что таких данных в текущей выборке нет
            if (!currentSample.getSampleData().contains(sampleData)) {
                samples.add(sampleData);
            }
        }

        if (currentSample != newSample) {
            try {
                // Добавляем только новые данные
                samplesManager.addSampleData(samples);
                currentSample.getSampleData().addAll(samples);
            } catch (SQLException e) {
                searchView.showErrorMessage("SQL Ошибка во время создания новой выборки");
                Logger.getGlobal().log(Level.SEVERE, "", e);
            }
        }
    }

    @Override
    public void handleDeleteSampleData(Collection selectedRows) {
        if (selectedRows == null || selectedRows.isEmpty()) {
            searchView.showErrorMessage("Ничего не выбрано для удаления из выборки!");
            return;
        }
        if (currentSample != newSample) {
            ArrayList<Long> uniqueIdentifersSelected = new ArrayList<>();
            for (Integer o : (Collection<Integer>) selectedRows) {
                // Не забываем, что selectFieldsId.get(0) - уникальный проперти UniqueId, невидимый в результатах
                Long uniqueId = uniqueIdentifiers.get(o);
                uniqueIdentifersSelected.add(uniqueId);
            }
            List<SampleData> sampleDataList = currentSample.getSampleData()
                    .stream()
                    .filter(sampleData -> uniqueIdentifersSelected.contains(sampleData.getUnique_id()))
                    .collect(Collectors.toList());
            try {
                samplesManager.removeFromSample(sampleDataList);
                currentSample.getSampleData().removeAll(sampleDataList);
                selectedRows.forEach(lastResult::removeItem);
            } catch (SQLException e) {
                searchView.showErrorMessage("Ошибка во время попытки удалить данные выборки!");
            }
        }
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
        try {
            currentSample.setName(name);
            currentSample.setComments(comment);
            currentSample.setUser_id(session.getSysAgents().getN());
            // Добавляем в базу
            samplesManager.addSample(currentSample);

            // Заново создаем шаблонную выборку, готовую к работе
            newSample = getNewTemplateSample();
            userSamples.addItemAt(0, newSample);

            // Переходим в режим работы с выборкой (SelectTarget остался нужным)
            searchView.setSampleComponentsEnable(true);
        } catch (SQLException e) {
            searchView.showErrorMessage("SQL Ошибка во время создания новой выборки");
            Logger.getGlobal().log(Level.SEVERE, "", e);
        }
    }

    @Override
    public BeanItemContainer<Sample> getSamples() {
        return userSamples;
    }

    /**
     * @return Новая шаблонная выборка
     */
    private Sample getNewTemplateSample() {
        Sample sample = new Sample();
        sample.setName("Новая выборка");
        sample.setSearchTarget(Target.PATIENT);
        sample.setAllSelectColumns(selectColumnManager.getViewForSearch(Target.PATIENT));
        sample.setAllCriteria(selectColumnManager.getAllCriteria());
        return sample;
    }

    @Override
    public void handleDeleteCurrentSample() {
        if (currentSample.getN() != -1) {
            searchView.showConfirmDialog("Удалить выборку " + currentSample.getName(),
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
            try {
                samplesManager.deleteCriteria(currentSample.getN());
                samplesManager.saveCriteria(currentSample);
            } catch (SQLException e) {
                searchView.showErrorMessage("Ошибка во время сохранения выбранных критериев");
            }
        }
    }

    @Override
    public void acceptSelectColumnsChange(List<SelectColumn> selectColumns) {
        if (currentSample != newSample) {
            try {
                samplesManager.deleteSelectColumns(currentSample.getN());
                samplesManager.saveSelectColumns(currentSample);
            } catch (SQLException e) {
                searchView.showErrorMessage("Ошибка во время сохранения выбранных столбцов");
            }
        }
    }

    private void deleteSample(Boolean isConfirmed) {
        if (isConfirmed) {
            try {
                samplesManager.deleteSample(currentSample.getN());
                userSamples.removeItem(currentSample);
                currentSample = newSample;
                refreshView();
            } catch (SQLException e) {
                searchView.showErrorMessage("SQL Ошибка во время удаления выборки");
                Logger.getGlobal().log(Level.SEVERE, "", e);
            }
        }
    }
}