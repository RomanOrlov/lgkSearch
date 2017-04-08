package lgk.nsbc.view.spectcrud;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.components.grid.FooterCell;
import com.vaadin.ui.components.grid.FooterRow;
import com.vaadin.ui.components.grid.HeaderCell;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.renderers.NumberRenderer;
import com.vaadin.ui.themes.ValoTheme;
import lombok.Getter;
import org.springframework.context.annotation.Scope;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static lgk.nsbc.model.spect.ContourType.*;
import static lgk.nsbc.model.spect.MainInfo.*;
import static lgk.nsbc.model.spect.TargetType.*;

@SpringComponent
@Scope("prototype")
public class SpectGrid extends Grid<SpectGridData> {
    private List<Column<SpectGridData, Double>> mainInfoColumns;
    private List<Column<SpectGridData, Double>> hypColumns;
    private List<Column<SpectGridData, Double>> hizColumns;
    private List<Column<SpectGridData, Double>> targetsColumns;
    private List<Column<SpectGridData, Double>> volumeColumns;
    private List<Column<SpectGridData, Double>> min30Columns;
    private List<Column<SpectGridData, Double>> min60Columns;
    private List<Column<SpectGridData, Double>> sphereColumns;
    private List<Column<SpectGridData, Double>> isoline10Columns;
    private List<Column<SpectGridData, Double>> isoline25Columns;
    private List<Column<SpectGridData, Double>> inColumns;

    private DecimalFormat doubleFormat = new DecimalFormat("###0.00");

    @Getter
    private List<SpectGridData> allItems = new ArrayList<>();

    public SpectGrid() {
        ListDataProvider<SpectGridData> dataProvider = new ListDataProvider<>(getAllItems());
        setItems();
        setDataProvider(dataProvider);
        setSizeFull();
        setReadOnly(false);
        setColumnReorderingAllowed(false);
        // By default all columns is NOT hidable
        Column<SpectGridData, String> surnameColumn = addColumn(SpectGridData::getSurname)
                .setCaption("Фамилия")
                .setHidable(true);
        Column<SpectGridData, String> nameColumn = addColumn(SpectGridData::getName)
                .setCaption("Имя")
                .setHidable(true)
                .setHidden(true);
        Column<SpectGridData, String> patronumicColumn = addColumn(SpectGridData::getPatronymic)
                .setCaption("Отчество")
                .setHidable(true)
                .setHidden(true);
        Column<SpectGridData, String> caseHistoryNum = addColumn(SpectGridData::getCaseHistoryNum)
                .setCaption("Номер истории")
                .setHidable(true)
                .setHidden(true);
        Column<SpectGridData, LocalDate> studyDate = addColumn(SpectGridData::getStudyDate)
                .setCaption("Дата исследования")
                .setHidable(true);
        Column<SpectGridData, String> targetName = addColumn(SpectGridData::getTargetName)
                .setCaption("Мишень")
                .setHidable(true);
        Column<SpectGridData, Double> hypVolume = addColumn(SpectGridData::getHypVolume)
                .setCaption(VOLUME.getName());
        Column<SpectGridData, Double> hypMin30 = addColumn(SpectGridData::getHypMin30)
                .setCaption(MIN30.getName());
        Column<SpectGridData, Double> hypMin60 = addColumn(SpectGridData::getHypMin60)
                .setCaption(MIN60.getName());
        Column<SpectGridData, Double> hizSphereVolume = addColumn(SpectGridData::getHizSphereVolume)
                .setCaption(VOLUME.getName());
        Column<SpectGridData, Double> hizSphereMin30 = addColumn(SpectGridData::getHizSphereMin30)
                .setCaption(MIN30.getName());
        Column<SpectGridData, Double> hizSphereMin60 = addColumn(SpectGridData::getHizSphereMin60)
                .setCaption(MIN60.getName());
        Column<SpectGridData, Double> hizIsoline10Volume = addColumn(SpectGridData::getHizIsoline10Volume)
                .setCaption(VOLUME.getName());
        Column<SpectGridData, Double> hizIsoline10Min30 = addColumn(SpectGridData::getHizIsoline10Min30)
                .setCaption(MIN30.getName());
        Column<SpectGridData, Double> hizIsoline10Min60 = addColumn(SpectGridData::getHizIsoline10Min60)
                .setCaption(MIN60.getName());
        Column<SpectGridData, Double> hizIsoline25Volume = addColumn(SpectGridData::getHizIsoline25Volume)
                .setCaption(VOLUME.getName());
        Column<SpectGridData, Double> hizIsoline25Min30 = addColumn(SpectGridData::getHizIsoline25Min30)
                .setCaption(MIN30.getName());
        Column<SpectGridData, Double> hizIsoline25Min60 = addColumn(SpectGridData::getHizIsoline25Min60)
                .setCaption(MIN60.getName());
        Column<SpectGridData, Double> targetSphereVolume = addColumn(SpectGridData::getTargetSphereVolume)
                .setCaption(VOLUME.getName());
        Column<SpectGridData, Double> targetSphereMin30 = addColumn(SpectGridData::getTargetSphereMin30)
                .setCaption(MIN30.getName());
        Column<SpectGridData, Double> targetSphereMin60 = addColumn(SpectGridData::getTargetSphereMin60)
                .setCaption(MIN60.getName());
        Column<SpectGridData, Double> targetIsoline10Volume = addColumn(SpectGridData::getTargetIsoline10Volume)
                .setCaption(VOLUME.getName());
        Column<SpectGridData, Double> targetIsoline10Min30 = addColumn(SpectGridData::getTargetIsoline10Min30)
                .setCaption(MIN30.getName());
        Column<SpectGridData, Double> targetIsoline10Min60 = addColumn(SpectGridData::getTargetIsoline10Min60)
                .setCaption(MIN60.getName());
        Column<SpectGridData, Double> targetIsoline25Volume = addColumn(SpectGridData::getTargetIsoline25Volume)
                .setCaption(VOLUME.getName());
        Column<SpectGridData, Double> targetIsoline25Min30 = addColumn(SpectGridData::getTargetIsoline25Min30)
                .setCaption(MIN30.getName());
        Column<SpectGridData, Double> targetIsoline25Min60 = addColumn(SpectGridData::getTargetIsoline25Min60)
                .setCaption(MIN60.getName());

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


        volumeColumns = mainInfoColumns.stream()
                .filter(column -> column.getCaption().equals(VOLUME.getName()))
                .collect(toList());
        min30Columns = mainInfoColumns.stream()
                .filter(column -> column.getCaption().equals(MIN30.getName()))
                .collect(toList());
        min60Columns = mainInfoColumns.stream()
                .filter(column -> column.getCaption().equals(MIN60.getName()))
                .collect(toList());

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
        contourTypeHeader.setStyleName("align-center");
        HeaderRow structureTypeHeader = configureStructureHeaderRow();
        structureTypeHeader.setStyleName("align-center");
        // Наведение красоты
        structureTypeHeader.join(nameColumn, patronumicColumn, caseHistoryNum, studyDate, targetName);
        contourTypeHeader.join(nameColumn, patronumicColumn, caseHistoryNum, studyDate, targetName);
        addTextFilter(filterHeader, surnameColumn, dataProvider);
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
        setFrozenColumnCount(1);
    }

    private void setMeanInColumn(FooterCell footerCell, ToDoubleFunction<? super SpectGridData> getValue, List<SpectGridData> data) {
        double meanValue = data.stream()
                .mapToDouble(getValue)
                .average().orElse(0);
        footerCell.setText(doubleFormat.format(meanValue));
    }

    private HeaderRow configureContourHeaderRow() {
        HeaderRow structureTypeHeader = prependHeaderRow();
        for (int i = 0; i < sphereColumns.size(); i += 3) {
            HeaderCell join = structureTypeHeader.join(sphereColumns.get(i), sphereColumns.get(i + 1), sphereColumns.get(i + 2));
            join.setText(SPHERE.getName());
            join.setStyleName("align-center");
        }
        for (int i = 0; i < isoline10Columns.size(); i += 3) {
            HeaderCell join = structureTypeHeader.join(isoline10Columns.get(i), isoline10Columns.get(i + 1), isoline10Columns.get(i + 2));
            join.setText(ISOLYNE10.getName());
            join.setStyleName("align-center");
        }
        for (int i = 0; i < isoline25Columns.size(); i += 3) {
            HeaderCell join = structureTypeHeader.join(isoline25Columns.get(i), isoline25Columns.get(i + 1), isoline25Columns.get(i + 2));
            join.setText(ISOLYNE25.getName());
            join.setStyleName("align-center");
        }
        return structureTypeHeader;
    }

    private HeaderRow configureStructureHeaderRow() {
        HeaderRow targetTypeHeader = prependHeaderRow();

        HeaderCell targetJoinCell = targetTypeHeader.join((Column<?, ?>[]) (targetsColumns).toArray());
        targetJoinCell.setText(TARGET.getName());
        targetJoinCell.setStyleName("align-center");

        HeaderCell hizJoinCell = targetTypeHeader.join((Column<?, ?>[]) (hizColumns).toArray());
        hizJoinCell.setText(HIZ.getName());
        hizJoinCell.setStyleName("align-center");

        HeaderCell hypJoinCell = targetTypeHeader.join((Column<?, ?>[]) (hypColumns).toArray());
        hypJoinCell.setText(HYP.getName());
        hypJoinCell.setStyleName("align-center");

        HeaderCell inJoin = targetTypeHeader.join((Column<?, ?>[]) (inColumns).toArray());
        inJoin.setText("ИН");
        inJoin.setStyleName("align-center");
        return targetTypeHeader;
    }

    private void addTextFilter(HeaderRow filterHeader, Column<?, ?> column, ListDataProvider<SpectGridData> dataProvider) {
        HeaderCell cell = filterHeader.getCell(column);
        TextField filterField = getColumnTextFilterField();
        cell.setComponent(filterField);
        filterField.addValueChangeListener(event -> {
            String enteredValue = event.getValue();
            dataProvider.setFilter(SpectGridData::getSurname, surname -> {
                if (surname == null) return false;
                return surname.contains(enteredValue);
            });
        });
    }

    private TextField getColumnTextFilterField() {
        TextField filter = new TextField();
        filter.setWidth("100%");
        filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
        filter.setPlaceholder("Фильтр");
        return filter;
    }

    /**
     * Управление видимостью группы столбцов. Крайне необходимо из за внушающего количества столбцов
     *
     * @param columnNames Имена групп стобцов которые надо сделать невидимыми
     */
    public void updateColumnsVisibility(Set<String> columnNames) {
        mainInfoColumns.forEach(column -> column.setHidden(false));
        inColumns.forEach(column -> column.setHidden(false));
        for (String columnName : columnNames) {
            switch (columnName) {
                case "Сфера":
                    sphereColumns.forEach(column -> column.setHidden(true));
                    break;
                case "Изолиния 10":
                    isoline10Columns.forEach(column -> column.setHidden(true));
                    break;
                case "Изолиния 25":
                    isoline25Columns.forEach(column -> column.setHidden(true));
                    break;
                case "Хороидальное сплетение":
                    hizColumns.forEach(column -> column.setHidden(true));
                    break;
                case "Опухоль":
                    targetsColumns.forEach(column -> column.setHidden(true));
                    break;
                case "Гипофиз":
                    hypColumns.forEach(column -> column.setHidden(true));
                    break;
                case "Объем":
                    volumeColumns.forEach(column -> column.setHidden(true));
                    break;
                case "30 минут":
                    min30Columns.forEach(column -> column.setHidden(true));
                    break;
                case "60 минут":
                    min60Columns.forEach(column -> column.setHidden(true));
                    break;
                case "ИН":
                    inColumns.forEach(column -> column.setHidden(true));
                    break;
            }
        }
    }
}
