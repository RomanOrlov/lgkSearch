package lgk.nsbc.view.spectcrud;

import com.vaadin.data.Binder;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.provider.GridSortOrderBuilder;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.server.Setter;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.*;
import com.vaadin.ui.renderers.NumberRenderer;
import lgk.nsbc.dao.SpectDataManager;
import lgk.nsbc.model.NbcPatients;
import lgk.nsbc.model.NbcTarget;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static lgk.nsbc.model.spect.ContourType.*;
import static lgk.nsbc.model.spect.MainInfo.*;
import static lgk.nsbc.model.spect.TargetType.*;

@SpringComponent
@Scope("prototype")
public class SpectGrid extends Grid<SpectGridData> {
    List<Column<SpectGridData, Double>> mainInfoColumns;
    List<Column<SpectGridData, Double>> hypColumns;
    List<Column<SpectGridData, Double>> hizColumns;
    List<Column<SpectGridData, Double>> targetsColumns;
    List<Column<SpectGridData, Double>> volumeColumns;
    List<Column<SpectGridData, Double>> min30Columns;
    List<Column<SpectGridData, Double>> min60Columns;
    List<Column<SpectGridData, Double>> sphereColumns;
    List<Column<SpectGridData, Double>> isoline10Columns;
    List<Column<SpectGridData, Double>> isoline25Columns;
    List<Column<SpectGridData, Double>> inColumns;
    @Autowired
    private SpectDataManager spectDataManager;

    private DecimalFormat doubleFormat = new DecimalFormat("###0.00");

    @Getter
    private List<SpectGridData> allItems = new ArrayList<>();

    @PostConstruct
    public void init() {
        ListDataProvider<SpectGridData> dataProvider = new ListDataProvider<>(getAllItems());
        setDataProvider(dataProvider);
        setSizeFull();
        setReadOnly(false);
        setColumnReorderingAllowed(false);
        setSelectionMode(SelectionMode.SINGLE);

        // By default all columns is NOT hidable
        Column<SpectGridData, String> surnameColumn = addColumn(SpectGridData::getSurname)
                .setCaption("Фамилия")
                .setHidable(true);
        Column<SpectGridData, String> nameColumn = addColumn(SpectGridData::getName)
                .setCaption("Имя")
                .setHidable(true)
                .setHidden(true);
        Column<SpectGridData, String> patronymicColumn = addColumn(SpectGridData::getPatronymic)
                .setCaption("Отчество")
                .setHidable(true)
                .setHidden(true);
        Column<SpectGridData, String> caseHistoryNum = addColumn(SpectGridData::getCaseHistoryNum)
                .setCaption("Номер истории")
                .setHidable(true)
                .setHidden(true);
        Column<SpectGridData, LocalDate> studyDate = addColumn(SpectGridData::getStudyDate)
                .setCaption("Дата исследования")
                .setHidable(true)
                .setEditorBinding(getStudyDateBind());
        Column<SpectGridData, NbcTarget> targetName = addColumn(SpectGridData::getTarget)
                .setCaption("Мишень")
                .setHidable(true)
                .setEditorBinding(getTargetBind());
        addColumn(SpectGridData::getDose)
                .setCaption("Доза")
                .setHidable(true)
                .setEditorBinding(getDoubleBind(SpectGridData::getDose, SpectGridData::setDose));

        Column<SpectGridData, Double> hypVolume = addColumn(SpectGridData::getHypVolume)
                .setCaption(VOLUME.getName())
                .setEditorBinding(getDoubleBind(SpectGridData::getHypVolume, SpectGridData::setHypVolume));
        Column<SpectGridData, Double> hypMin30 = addColumn(SpectGridData::getHypMin30)
                .setCaption(MIN30.getName())
                .setEditorBinding(getDoubleBind(SpectGridData::getHypMin30, SpectGridData::setHypMin30));
        Column<SpectGridData, Double> hypMin60 = addColumn(SpectGridData::getHypMin60)
                .setCaption(MIN60.getName())
                .setEditorBinding(getDoubleBind(SpectGridData::getHypMin60, SpectGridData::setHypMin60));
        Column<SpectGridData, Double> hizSphereVolume = addColumn(SpectGridData::getHizSphereVolume)
                .setCaption(VOLUME.getName())
                .setEditorBinding(getDoubleBind(SpectGridData::getHizSphereVolume, SpectGridData::setHizSphereVolume));
        Column<SpectGridData, Double> hizSphereMin30 = addColumn(SpectGridData::getHizSphereMin30)
                .setCaption(MIN30.getName())
                .setEditorBinding(getDoubleBind(SpectGridData::getHizSphereMin30, SpectGridData::setHizSphereMin30));
        Column<SpectGridData, Double> hizSphereMin60 = addColumn(SpectGridData::getHizSphereMin60)
                .setCaption(MIN60.getName())
                .setEditorBinding(getDoubleBind(SpectGridData::getHizSphereMin60, SpectGridData::setHizSphereMin60));
        Column<SpectGridData, Double> hizIsoline10Volume = addColumn(SpectGridData::getHizIsoline10Volume)
                .setCaption(VOLUME.getName())
                .setEditorBinding(getDoubleBind(SpectGridData::getHizIsoline10Volume, SpectGridData::setHizIsoline10Volume));
        Column<SpectGridData, Double> hizIsoline10Min30 = addColumn(SpectGridData::getHizIsoline10Min30)
                .setCaption(MIN30.getName())
                .setEditorBinding(getDoubleBind(SpectGridData::getHizIsoline10Min30, SpectGridData::setHizIsoline10Min30));
        Column<SpectGridData, Double> hizIsoline10Min60 = addColumn(SpectGridData::getHizIsoline10Min60)
                .setCaption(MIN60.getName())
                .setEditorBinding(getDoubleBind(SpectGridData::getHizIsoline10Min60, SpectGridData::setHizIsoline10Min60));
        Column<SpectGridData, Double> hizIsoline25Volume = addColumn(SpectGridData::getHizIsoline25Volume)
                .setCaption(VOLUME.getName())
                .setEditorBinding(getDoubleBind(SpectGridData::getHizIsoline25Volume, SpectGridData::setHizIsoline25Volume));
        Column<SpectGridData, Double> hizIsoline25Min30 = addColumn(SpectGridData::getHizIsoline25Min30)
                .setCaption(MIN30.getName())
                .setEditorBinding(getDoubleBind(SpectGridData::getHizIsoline25Min30, SpectGridData::setHizIsoline25Min30));
        Column<SpectGridData, Double> hizIsoline25Min60 = addColumn(SpectGridData::getHizIsoline25Min60)
                .setCaption(MIN60.getName())
                .setEditorBinding(getDoubleBind(SpectGridData::getHizIsoline25Min60, SpectGridData::setHizIsoline25Min60));
        Column<SpectGridData, Double> targetSphereVolume = addColumn(SpectGridData::getTargetSphereVolume)
                .setCaption(VOLUME.getName())
                .setEditorBinding(getDoubleBind(SpectGridData::getTargetSphereVolume, SpectGridData::setTargetSphereVolume));
        Column<SpectGridData, Double> targetSphereMin30 = addColumn(SpectGridData::getTargetSphereMin30)
                .setCaption(MIN30.getName())
                .setEditorBinding(getDoubleBind(SpectGridData::getTargetSphereMin30, SpectGridData::setTargetSphereMin30));
        Column<SpectGridData, Double> targetSphereMin60 = addColumn(SpectGridData::getTargetSphereMin60)
                .setCaption(MIN60.getName())
                .setEditorBinding(getDoubleBind(SpectGridData::getTargetSphereMin60, SpectGridData::setTargetSphereMin60));
        Column<SpectGridData, Double> targetIsoline10Volume = addColumn(SpectGridData::getTargetIsoline10Volume)
                .setCaption(VOLUME.getName())
                .setEditorBinding(getDoubleBind(SpectGridData::getTargetIsoline10Volume, SpectGridData::setTargetIsoline10Volume));
        Column<SpectGridData, Double> targetIsoline10Min30 = addColumn(SpectGridData::getTargetIsoline10Min30)
                .setCaption(MIN30.getName())
                .setEditorBinding(getDoubleBind(SpectGridData::getTargetIsoline10Min30, SpectGridData::setTargetIsoline10Min30));
        Column<SpectGridData, Double> targetIsoline10Min60 = addColumn(SpectGridData::getTargetIsoline10Min60)
                .setCaption(MIN60.getName())
                .setEditorBinding(getDoubleBind(SpectGridData::getTargetIsoline10Min60, SpectGridData::setTargetIsoline10Min60));
        Column<SpectGridData, Double> targetIsoline25Volume = addColumn(SpectGridData::getTargetIsoline25Volume)
                .setCaption(VOLUME.getName())
                .setEditorBinding(getDoubleBind(SpectGridData::getTargetIsoline25Volume, SpectGridData::setTargetIsoline25Volume));
        Column<SpectGridData, Double> targetIsoline25Min30 = addColumn(SpectGridData::getTargetIsoline25Min30)
                .setCaption(MIN30.getName())
                .setEditorBinding(getDoubleBind(SpectGridData::getTargetIsoline25Min30, SpectGridData::setTargetIsoline25Min30));
        Column<SpectGridData, Double> targetIsoline25Min60 = addColumn(SpectGridData::getTargetIsoline25Min60)
                .setCaption(MIN60.getName())
                .setEditorBinding(getDoubleBind(SpectGridData::getTargetIsoline25Min60, SpectGridData::setTargetIsoline25Min60));

        hypColumns = Arrays.asList(hypVolume, hypMin30, hypMin60);
        hizColumns = Arrays.asList(hizSphereVolume, hizSphereMin30, hizSphereMin60,
                hizIsoline10Volume, hizIsoline10Min30, hizIsoline10Min60,
                hizIsoline25Volume, hizIsoline25Min30, hizIsoline25Min60);
        targetsColumns = Arrays.asList(targetSphereVolume, targetSphereMin30, targetSphereMin60,
                targetIsoline10Volume, targetIsoline10Min30, targetIsoline10Min60,
                targetIsoline25Volume, targetIsoline25Min30, targetIsoline25Min60);
        mainInfoColumns = new ArrayList<>(hypColumns);
        mainInfoColumns.addAll(hizColumns);
        mainInfoColumns.addAll(targetsColumns);

        // Индекс накопления - генерируемые данные.
        Column<SpectGridData, Double> inEarly = addColumn(SpectGridData::getInEarly, new NumberRenderer(doubleFormat));
        inEarly.setCaption(MIN30.getName());
        Column<SpectGridData, Double> inLate = addColumn(SpectGridData::getInLate, new NumberRenderer(doubleFormat));
        inLate.setCaption(MIN60.getName());
        Column<SpectGridData, Double> inOut = addColumn(SpectGridData::getInOut, new NumberRenderer(doubleFormat));
        inOut.setCaption("Вымывание");
        inColumns = Arrays.asList(inEarly, inLate, inOut);

        splitByMainInfo();

        sphereColumns = Arrays.asList(hypVolume, hypMin30, hypMin60,
                hizSphereVolume, hizSphereMin30, hizSphereMin60,
                targetSphereVolume, targetSphereMin30, targetSphereMin60,
                inEarly, inLate, inOut);
        isoline10Columns = Arrays.asList(hizIsoline10Volume, hizIsoline10Min30, hizIsoline10Min60,
                targetIsoline10Volume, targetIsoline10Min30, targetIsoline10Min60);
        isoline25Columns = Arrays.asList(hizIsoline25Volume, hizIsoline25Min30, hizIsoline25Min60,
                targetIsoline25Volume, targetIsoline25Min30, targetIsoline25Min60);

        mainInfoColumns.forEach(spectGridDataDoubleColumn -> spectGridDataDoubleColumn.setHidable(false));
        HeaderRow filterHeader = prependHeaderRow();
        filterHeader.setStyleName("align-center");
        HeaderRow contourTypeHeader = configureContourHeaderRow();
        HeaderRow structureTypeHeader = configureStructureHeaderRow();
        // Наведение красоты
        structureTypeHeader.join(nameColumn, patronymicColumn, caseHistoryNum, studyDate, targetName);
        contourTypeHeader.join(nameColumn, patronymicColumn, caseHistoryNum, studyDate, targetName);
        ColumnsFilterUtil.addTextFilter(filterHeader, surnameColumn, dataProvider);
        // Заполняем маленькую статистику
        FooterRow footerRow = appendFooterRow();
        dataProvider.addDataProviderListener(event -> {
            List<SpectGridData> data = event.getSource()
                    .fetch(new Query<>())
                    .collect(Collectors.toList());
            footerRow.getCell(surnameColumn).setText("Всего " + Integer.toString(data.size()));
            setMeanInColumn(footerRow.getCell(inEarly), SpectGridData::getInEarly, data);
            setMeanInColumn(footerRow.getCell(inLate), SpectGridData::getInLate, data);
            setMeanInColumn(footerRow.getCell(inOut), SpectGridData::getInOut, data);

            setMeanInColumn(footerRow.getCell(hizSphereVolume), SpectGridData::getHizSphereVolume, data);
            setMeanInColumn(footerRow.getCell(hizSphereMin30), SpectGridData::getHizSphereMin30, data);
            setMeanInColumn(footerRow.getCell(hizSphereMin60), SpectGridData::getHizSphereMin60, data);

            setMeanInColumn(footerRow.getCell(targetSphereVolume), SpectGridData::getTargetSphereVolume, data);
            setMeanInColumn(footerRow.getCell(targetSphereMin30), SpectGridData::getTargetSphereMin30, data);
            setMeanInColumn(footerRow.getCell(targetSphereMin60), SpectGridData::getTargetSphereMin60, data);
        });
        dataProvider.refreshAll();
        configureEditor();
        setFrozenColumnCount(1);
        setSortOrder(new GridSortOrderBuilder<SpectGridData>().thenAsc(surnameColumn)
                .thenAsc(studyDate));
    }

    private void splitByMainInfo() {
        volumeColumns = mainInfoColumns.stream()
                .filter(column -> column.getCaption().equals(VOLUME.getName()))
                .collect(toList());
        min30Columns = mainInfoColumns.stream()
                .filter(column -> column.getCaption().equals(MIN30.getName()))
                .collect(toList());
        min60Columns = mainInfoColumns.stream()
                .filter(column -> column.getCaption().equals(MIN60.getName()))
                .collect(toList());
    }

    private Binder.Binding<SpectGridData, Double> getDoubleBind(ValueProvider<SpectGridData, Double> valueProvider, Setter<SpectGridData, Double> setter) {
        TextField bindField = new TextField();
        return getEditor()
                .getBinder()
                .forField(bindField)
                .withNullRepresentation("")
                .withConverter(new StringToDoubleConverter("Некорректное значение поля"))
                .bind(valueProvider, setter);
    }

    private Binder.Binding<SpectGridData, LocalDate> getStudyDateBind() {
        DateField dateField = new DateField();
        dateField.setTextFieldEnabled(false);
        dateField.setRequiredIndicatorVisible(true);
        return getEditor()
                .getBinder()
                .forField(dateField)
                .withNullRepresentation(LocalDate.now())
                .asRequired("Исследование должно иметь дату")
                .bind(SpectGridData::getStudyDate, SpectGridData::setStudyDate);
    }

    private Binder.Binding<SpectGridData, NbcTarget> getTargetBind() {
        NativeSelect<NbcTarget> targets = new NativeSelect<>("Мишень");
        targets.setEmptySelectionAllowed(false);
        targets.setRequiredIndicatorVisible(true);
        addItemClickListener(event -> {
            System.out.println(event.getMouseEventDetails().isDoubleClick());
            SpectGridData item = event.getItem();
            targets.setItems(item.getSpectGridDBData().getTargets());
        });
        return getEditor()
                .getBinder()
                .forField(targets)
                .asRequired("Мишень должна быть выбрана")
                .withNullRepresentation(NbcTarget.builder()
                        .n(-1L)
                        .nbc_patients_n(-1L)
                        .targetName("Мишень не выбрана")
                        .targetType(-1L)
                        .build()
                )
                .bind(SpectGridData::getTarget, SpectGridData::setTarget);
    }

    private void configureEditor() {
        Editor<SpectGridData> editor = getEditor();
        editor.setEnabled(true);
        editor.setSaveCaption("Сохранить");
        editor.setCancelCaption("Отмена");
        editor.setBuffered(true);
        editor.addSaveListener(event -> {
            if (editor.getBinder().hasChanges()) {
                spectDataManager.deleteSpectData(event.getBean());
                spectDataManager.persistSpectData(event.getBean());
                refreashAllData();
            }
        });
    }

    private void setMeanInColumn(FooterCell footerCell, Function<? super SpectGridData, Double> getValue, List<SpectGridData> data) {
        // TODO странная ошибка в DoubleStream при average()
        List<Double> collect = data.stream()
                .map(getValue::apply)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Double meanValue;
        if (collect.isEmpty())
            meanValue = 0.d;
        else {
            meanValue = collect.stream()
                    .mapToDouble(value -> value)
                    .average().orElse(0.d);
        }
        footerCell.setText(doubleFormat.format(meanValue));
    }

    private HeaderRow configureContourHeaderRow() {
        HeaderRow structureTypeHeader = prependHeaderRow();
        for (int i = 0; i < sphereColumns.size(); i += 3) {
            HeaderCell join = structureTypeHeader.join(sphereColumns.get(i), sphereColumns.get(i + 1), sphereColumns.get(i + 2));
            join.setText(SPHERE.getName());
        }
        for (int i = 0; i < isoline10Columns.size(); i += 3) {
            HeaderCell join = structureTypeHeader.join(isoline10Columns.get(i), isoline10Columns.get(i + 1), isoline10Columns.get(i + 2));
            join.setText(ISOLYNE10.getName());
        }
        for (int i = 0; i < isoline25Columns.size(); i += 3) {
            HeaderCell join = structureTypeHeader.join(isoline25Columns.get(i), isoline25Columns.get(i + 1), isoline25Columns.get(i + 2));
            join.setText(ISOLYNE25.getName());
        }
        return structureTypeHeader;
    }

    private HeaderRow configureStructureHeaderRow() {
        HeaderRow targetTypeHeader = prependHeaderRow();

        HeaderCell targetJoinCell = targetTypeHeader.join((Column<?, ?>[]) (targetsColumns).toArray());
        targetJoinCell.setText(TARGET.getName());

        HeaderCell hizJoinCell = targetTypeHeader.join((Column<?, ?>[]) (hizColumns).toArray());
        hizJoinCell.setText(HIZ.getName());

        HeaderCell hypJoinCell = targetTypeHeader.join((Column<?, ?>[]) (hypColumns).toArray());
        hypJoinCell.setText(HYP.getName());

        HeaderCell inJoin = targetTypeHeader.join((Column<?, ?>[]) (inColumns).toArray());
        inJoin.setText("ИН");
        return targetTypeHeader;
    }

    public void addNewSpecDataRecord(NbcPatients nbcPatients) {
        SpectGridData blankSpectGridData = spectDataManager.getBlankSpectGridData(nbcPatients);
        allItems.add(0, blankSpectGridData);
        getDataProvider().refreshAll();
        select(blankSpectGridData);
        Notification.show("Дважды кликните для редактирования", Notification.Type.TRAY_NOTIFICATION);
    }

    public void refreashAllData() {
        allItems.clear();
        allItems.addAll(spectDataManager.findAllData());
        getDataProvider().refreshAll();
    }

    public void deleteSelected() {
        if (asSingleSelect().isEmpty()) return;
        deselectAll();
        SpectGridData value = asSingleSelect().getValue();
        allItems.remove(value);
        getDataProvider().refreshAll();
    }
}
