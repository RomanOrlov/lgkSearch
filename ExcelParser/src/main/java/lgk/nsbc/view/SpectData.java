package lgk.nsbc.view;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.ui.Grid;
import lgk.nsbc.model.StructureType;
import lgk.nsbc.template.dao.*;
import lgk.nsbc.template.model.*;
import lgk.nsbc.template.model.spect.ContourType;
import lgk.nsbc.template.model.spect.MainInfo;
import lgk.nsbc.template.model.spect.TargetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.*;
import java.util.stream.Collectors;

import static lgk.nsbc.template.model.spect.ContourType.ISOLYNE10;
import static lgk.nsbc.template.model.spect.ContourType.ISOLYNE25;
import static lgk.nsbc.template.model.spect.MainInfo.*;

@org.springframework.stereotype.Component
@Scope("prototype")
public class SpectData extends Grid {
    // Item id is NbcFlupSpect.N
    private Container.Indexed container;
    @Autowired
    private NbcStudDao nbcStudDao;
    @Autowired
    private NbcFollowUpDao nbcFollowUpDao;
    @Autowired
    private NbcFlupSpectDao getNbcFlupSpectDao;
    @Autowired
    private NbcFlupSpectDataDao nbcFlupSpectDataDao;
    @Autowired
    private NbcTargetDao nbcTargetDao;
    @Autowired
    private NbcFlupSpectDao nbcFlupSpectDao;

    // Key - имя фильтра, Value - паттерн propertyId по которому искать нужные столбцы
    // Фильтры работают для всех столбцов, чьи проперти не начинаются с #
    private Map<String, String> filters;

    public SpectData() {
        Grid.HeaderRow structureTypeHeader = addHeaderRowAt(0);
        Grid.HeaderRow targetTypeHeader = addHeaderRowAt(0);
        setSelectionMode(SelectionMode.MULTI);
        Grid.Column date = addColumn("#date", Date.class);
        date.setHidable(true);
        date.setEditable(false);
        date.setHeaderCaption("Дата исследования");

        Grid.Column target = addColumn("#nbctarget", String.class);
        target.setHidable(true);
        target.setEditable(false);
        target.setHeaderCaption("Мишень");

        List<Column> columns = new ArrayList<>();
        nextTarget:
        for (TargetType targetType : TargetType.values()) {
            for (ContourType contourType : ContourType.values()) {
                for (MainInfo mainInfo : MainInfo.values()) {
                    String propertyId = targetType.toString() + contourType.toString() + mainInfo.toString();
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

        Column idColumn = addColumn("#id", Long.class);
        idColumn.setHidden(true);
        idColumn.setHidable(false);

        setSizeFull();
        container = getContainerDataSource();
        prepareFilters();
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
                .map(item -> item.getItemProperty("id").getValue())
                .map(o -> (Long) o)
                .collect(Collectors.toList());
        nbcFlupSpectDataDao.deleteByNbcFlupSpectId(id);
    }

    public void readData(NbcPatients selectedPatient) {
        container.removeAllItems();
        List<NbcStud> patientsSpectStudy = nbcStudDao.findPatientsSpectStudy(selectedPatient);
        // ОФЕКТ исследования они, по сути, независимы
        for (NbcStud nbcStud : patientsSpectStudy) {
            List<NbcFollowUp> nbcFollowUps = nbcFollowUpDao.findByStudy(nbcStud);
            for (NbcFollowUp nbcFollowUp : nbcFollowUps) {
                NbcFlupSpect nbcFlupSpect = nbcFlupSpectDao.findByFollowUp(nbcFollowUp);
                NbcTarget nbcTarget = nbcTargetDao.findTargetById(nbcFollowUp.getNbc_target_n());
                // А это уже непосредственно записи в таблице
                List<NbcFlupSpectData> rowData = nbcFlupSpectDataDao.findBySpectFlup(nbcFlupSpect);
                RowData data = RowData.builder().datas(rowData)
                        .nbcTarget(nbcTarget)
                        .nbcStud(nbcStud)
                        .nbcFlupSpect(nbcFlupSpect)
                        .nbcFollowUp(nbcFollowUp)
                        .build();
                // id - NbcFlupSpect.N
                Item item = container.addItem(nbcFlupSpect.getN());
                data.fillItem(item);
            }
        }
    }

    private void prepareFilters() {
        filters = new HashMap<>();
        for (TargetType targetType : TargetType.values()) {
            filters.put(targetType.getName(), targetType.toString());
        }
        for (ContourType contourType : ContourType.values()) {
            filters.put(contourType.getName(), contourType.toString());
        }
    }

    public Set<String> getFilters() {
        return filters.keySet();
    }

    /**
     * Выбор невидимость группы столбцов
     * Например, - Опухоль. Тогда все столбы которые отображают данные опухоли будут невидны.
     *
     * @param names Именна группы невидимых столбцов
     */
    public void updateVisibility(Set<String> names) {
        // Получаем часть propertyId
        Set<String> invisible = names.stream().map(s -> {
            for (ContourType contourType : ContourType.values()) {
                if (contourType.getName().equals(s))
                    return contourType.toString();
            }
            for (TargetType targetType : TargetType.values()) {
                if (targetType.getName().equals(s))
                    return targetType.toString();
            }
            return s;
        }).collect(Collectors.toSet());
        List<Column> filterableColumns = getColumns().stream()
                .filter(column -> !((String) column.getPropertyId()).startsWith("#"))
                .collect(Collectors.toList());

        List<Column> invisibleColumns = filterableColumns.stream()
                .filter(column -> containsAnyInSet((String) column.getPropertyId(), invisible))
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
        return (Long) container.getItem(selectedRow).getItemProperty("id").getValue();
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
            item.getItemProperty("#date").setValue(nbcStud.getStudydatetime());
            item.getItemProperty("#nbctarget").setValue(nbcTarget.getTargetname());
            item.getItemProperty("id").setValue(nbcFlupSpect.getN());
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
