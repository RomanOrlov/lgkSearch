package lgk.nsbc.util;

import com.vaadin.ui.Grid;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.data.Container;
import lgk.nsbc.dao.*;
import lgk.nsbc.model.*;
import lgk.nsbc.model.spect.ContourType;
import lgk.nsbc.model.spect.MainInfo;
import lgk.nsbc.model.spect.TargetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static lgk.nsbc.model.spect.MainInfo.*;

@Component
@Scope("prototype")
public class SpectData extends Grid {
    // Item id is NbcFlupSpect.N
    private Container.Indexed container;
    @Autowired
    private NbcStudDao nbcStudDao;
    @Autowired
    private NbcFollowUpDao nbcFollowUpDao;
    @Autowired
    private NbcFlupSpectDataDao nbcFlupSpectDataDao;
    @Autowired
    private NbcTargetDao nbcTargetDao;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    public SpectData() {
        HeaderRow structureTypeHeader = addHeaderRowAt(0);
        HeaderRow targetTypeHeader = addHeaderRowAt(0);
        addHidebleColumn("#date", String.class, "Дата исследования");
        addHidebleColumn("#nbctarget", String.class, "Мишень");
        addHiddenColumn("#id", Long.class);

        List<Column> columns = new ArrayList<>();
        nextTarget:
        for (TargetType targetType : TargetType.values()) {
            for (ContourType contourType : ContourType.values()) {
                for (MainInfo mainInfo : MainInfo.values()) {
                    String propertyId = targetType.toString() + contourType.toString() + mainInfo.toString();
                    addColumn()
                    Grid.Column column = addColumn(propertyId, Double.class);
                    column.setHeaderCaption(mainInfo.getName());
                    column.setHidable(false);
                    column.setResizable(false);
                    column.setEditable(false);
                    columns.add(column);
                }
                // Если гипофиз то только сфера
                if (targetType == TargetType.HYP) continue nextTarget;
            }
        }

        // Заголовок Опухоль, Гипофиз, Хороидальное сплетение
        for (TargetType targetType : TargetType.values()) {
            List<Object> propertyId = getColumnsProperty(columns, targetType.toString());
            Grid.HeaderCell join = targetTypeHeader.join(propertyId.toArray());
            join.setText(targetType.getName());
        }

        // Заголовок Сфера, Изолиния 10, Изолиния 25
        for (ContourType contourType : ContourType.values()) {
            List<Object> propertyId = getColumnsProperty(columns, contourType.toString());
            if (propertyId.size() % 3 != 0) throw new RuntimeException("Несовпадение размера ячеек");
            for (int i = 0; i < propertyId.size(); i += 3) {
                Grid.HeaderCell join = structureTypeHeader.join(propertyId.get(i), propertyId.get(i + 1), propertyId.get(i + 2));
                join.setText(contourType.getName());
            }
        }

        setSizeFull();
        container = getContainerDataSource();
    }

    public Column addHidebleColumn(String propertyId, Class<?> aClass, String headerCaption) {
        Column column = addColumn(propertyId, aClass);
        column.setHeaderCaption(headerCaption);
        column.setHidable(true);
        column.setEditable(false);
        return column;
    }

    public Column addHiddenColumn(String propertyId, Class<?> aClass) {
        Column column = addColumn(propertyId, aClass);
        column.setHidden(true);
        column.setHidable(false);
        return column;
    }

    private List<Object> getColumnsProperty(List<Column> columns, String filter) {
        return columns.stream()
                .map(column -> ((String) column.getPropertyId()))
                .filter(s -> !s.startsWith("#"))
                .filter(s -> s.contains(filter))
                .collect(Collectors.toList());
    }

    /**
     * Вытаскиваем id выбранных строк и удаляем из базы и контейнера
     */
    public void deleteSelectedRecords() {
        List<Long> id = getSelectedRows().stream()
                .map(container::getItem)
                .peek(container::removeItem)
                .map(item -> item.getItemProperty("#id").getValue())
                .map(o -> (Long) o)
                .collect(Collectors.toList());
        nbcFlupSpectDataDao.deleteByNbcFlupSpectId(id);
        deselectAll();
    }

    public void readData(NbcPatients selectedPatient) {
        deselectAll();
        container.removeAllItems();
        List<NbcStud> patientsSpectStudy = nbcStudDao.findPatientsSpectStudy(selectedPatient);
        // ОФЕКТ исследования они, по сути, независимы
        for (NbcStud nbcStud : patientsSpectStudy) {
            List<NbcFollowUp> nbcFollowUps = nbcFollowUpDao.findByStudy(nbcStud);
            for (NbcFollowUp nbcFollowUp : nbcFollowUps) {
                Optional<NbcFlupSpect> nbcFlupSpect = nbcFlupSpectDao.findByFollowUp(nbcFollowUp);
                if (!nbcFlupSpect.isPresent()) continue;
                NbcTarget nbcTarget = nbcTargetDao.findTargetById(nbcFollowUp.getNbc_target_n());
                // А это уже непосредственно записи в таблице
                List<NbcFlupSpectData> rowData = nbcFlupSpectDataDao.findBySpectFlup(nbcFlupSpect.get());
                RowData data = RowData.builder().datas(rowData)
                        .nbcTarget(nbcTarget)
                        .nbcStud(nbcStud)
                        .nbcFlupSpect(nbcFlupSpect.get())
                        .nbcFollowUp(nbcFollowUp)
                        .build();
                // id - NbcFlupSpect.N
                Item item = container.addItem(nbcFlupSpect.get().getN());
                data.fillItem(item);
            }
        }
    }

    /**
     * Выбор невидимость группы столбцов
     * Например, - Опухоль. Тогда все столбы которые отображают данные опухоли будут невидны.
     *
     * @param propertyId Именна группы невидимых столбцов
     */
    public void updateVisibility(Set<String> propertyId) {
        List<Column> filterableColumns = getColumns().stream()
                .filter(column -> !((String) column.getPropertyId()).startsWith("#"))
                .collect(Collectors.toList());

        List<Column> invisibleColumns = filterableColumns.stream()
                .filter(column -> containsAnyInSet((String) column.getPropertyId(), propertyId))
                .peek(column -> column.setHidden(true))
                .collect(Collectors.toList());

        // Visible columns
        filterableColumns.stream()
                .filter(column -> !invisibleColumns.contains(column))
                .forEach(column -> column.setHidden(false));
    }

    private boolean containsAnyInSet(String s, Set<String> set) {
        return set.stream().anyMatch(s::contains);
    }

    public Long getSelectedRowId() {
        Object selectedRow = getSelectedRows().toArray()[0];
        return (Long) container.getItem(selectedRow).getItemProperty("#id").getValue();
    }

    /**
     * Преобразование данных из базы в таблицу
     */
    @Getter
    @AllArgsConstructor
    @Builder
    public static class RowData {
        private final List<NbcFlupSpectData> datas;
        private final NbcStud nbcStud;
        private final NbcFollowUp nbcFollowUp;
        private final NbcFlupSpect nbcFlupSpect;
        private final NbcTarget nbcTarget;

        public void fillItem(Item item) {
            // Заполняем вручную проперти.
            item.getItemProperty("#date").setValue(DATE_FORMAT.format(nbcStud.getStudydatetime()));
            item.getItemProperty("#nbctarget").setValue(nbcTarget.getTargetname());
            item.getItemProperty("#id").setValue(nbcFlupSpect.getN());
            // Разделяю данные на структуры
            for (TargetType targetType : TargetType.values()) {
                List<NbcFlupSpectData> targetData = datas.stream()
                        .filter(nbcFlupSpectData -> nbcFlupSpectData.getStructure_type().equals(targetType.getName()))
                        .collect(Collectors.toList());
                if (targetData.isEmpty()) continue;
                // Разделяю по контурам
                for (ContourType contourType : ContourType.values()) {
                    // Вставляю непосредственно весь хлам
                    List<NbcFlupSpectData> contourData = targetData.stream()
                            .filter(nbcFlupSpectData -> nbcFlupSpectData.getContour_type().equals(contourType.getName()))
                            .collect(Collectors.toList());
                    if (contourData.isEmpty()) continue;
                    if (contourData.size() > 1) throw new RuntimeException("Больше чем один набор данных");
                    NbcFlupSpectData data = contourData.get(0);
                    String firstIdPart = targetType.toString() + contourType.toString();
                    item.getItemProperty(firstIdPart + VOLUME.toString()).setValue(data.getVolume());
                    item.getItemProperty(firstIdPart + MIN30.toString()).setValue(data.getEarly_phase());
                    item.getItemProperty(firstIdPart + MIN60.toString()).setValue(data.getLate_phase());
                }
            }
        }
    }
}
