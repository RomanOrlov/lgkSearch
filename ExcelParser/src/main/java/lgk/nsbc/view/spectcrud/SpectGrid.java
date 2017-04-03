package lgk.nsbc.view.spectcrud;

import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.components.grid.FooterRow;
import com.vaadin.ui.components.grid.HeaderCell;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.context.annotation.Scope;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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

    public SpectGrid() {
        setSizeFull();
        setReadOnly(true);
        setColumnReorderingAllowed(false);

        Column<SpectGridData, String> surnameColumn = addColumn(SpectGridData::getSurname)
                .setCaption("Фамилия")
                .setHidable(true);
        Column<SpectGridData, String> nameColumn = addColumn(SpectGridData::getName)
                .setCaption("Имя")
                .setHidable(true);
        Column<SpectGridData, String> patronumicColumn = addColumn(SpectGridData::getPatronymic)
                .setCaption("Отчество")
                .setHidable(true);
        Column<SpectGridData, String> caseHistoryNum = addColumn(SpectGridData::getCaseHistoryNum)
                .setCaption("Номер истории")
                .setHidable(true);
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
                targetSphereVolume, targetSphereMin30, targetSphereMin60);
        isoline10Columns = Arrays.asList(hizIsoline10Volume, hizIsoline10Min30, hizIsoline10Min60,
                targetIsoline10Volume, targetIsoline10Min30, targetIsoline10Min60);
        isoline25Columns = Arrays.asList(hizIsoline25Volume, hizIsoline25Min30, hizIsoline25Min60,
                targetIsoline25Volume, targetIsoline25Min30, targetIsoline25Min60);

        mainInfoColumns.forEach(spectGridDataDoubleColumn -> spectGridDataDoubleColumn.setHidable(false));

        HeaderRow structureTypeHeader = configureStructureHeaderRow();
        HeaderRow contourTypeHeader = configureContourHeaderRow();
        // Наведение красоты
        structureTypeHeader.join(nameColumn, patronumicColumn, caseHistoryNum, studyDate, targetName);
        contourTypeHeader.join(nameColumn, patronumicColumn, caseHistoryNum, studyDate, targetName);
        HeaderRow filterHeader = addHeaderRowAt(2);
        addTextFilter(filterHeader, surnameColumn);
        FooterRow footerRow = addFooterRowAt(0);
        setFrozenColumnCount(1);
    }

    private HeaderRow configureContourHeaderRow() {
        HeaderRow structureTypeHeader = addHeaderRowAt(1);
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
        HeaderRow targetTypeHeader = addHeaderRowAt(0);
        HeaderCell targetJoinCell = targetTypeHeader.join((Column<?, ?>[]) (targetsColumns).toArray());
        targetJoinCell.setText(TARGET.getName());
        HeaderCell hizJoinCell = targetTypeHeader.join((Column<?, ?>[]) (hizColumns).toArray());
        hizJoinCell.setText(HIZ.getName());
        HeaderCell hypJoinCell = targetTypeHeader.join((Column<?, ?>[]) (hypColumns).toArray());
        hypJoinCell.setText(HYP.getName());
        return targetTypeHeader;
    }

    /**
     * Shit! It's seems to multiple filtering is in progress in Vaadin 8. Peace of crap!
     *
     * @param filterHeader
     * @param column
     */
    private void addTextFilter(HeaderRow filterHeader, Column<?, ?> column) {
        HeaderCell cell = filterHeader.getCell(column);
        TextField filterField = getColumnTextFilterField();
        cell.setComponent(filterField);
        filterField.addValueChangeListener(event -> {
            String enteredValue = event.getValue();
            DataProvider<SpectGridData, ?> dataProvider = getDataProvider();

            ConfigurableFilterDataProvider<SpectGridData, Void, ?> filter = dataProvider.withConfigurableFilter();
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
            }
        }
    }
}
