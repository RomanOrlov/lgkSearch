package lgk.nsbc.view;

import com.vaadin.ui.Grid;
import lgk.nsbc.template.model.NbcPatients;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@org.springframework.stereotype.Component
@Scope("prototype")
public class SpectData extends Grid{
    public SpectData() {
        super("SpectData");
        Grid.HeaderRow structureTypeHeader = addHeaderRowAt(0);
        Grid.HeaderRow targetTypeHeader = addHeaderRowAt(0);

        Grid.Column date = addColumn("date", Date.class);
        date.setHidable(false);
        date.setEditable(false);
        date.setHeaderCaption("Дата исследования");

        Grid.Column target = addColumn("nbctar", String.class);
        target.setHidable(false);
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

        for (TargetType targetType : TargetType.values()) {
            List<Object> propertyId = columns.stream()
                    .filter(column -> ((String) column.getPropertyId()).contains(targetType.toString()))
                    .map(Grid.Column::getPropertyId)
                    .collect(Collectors.toList());
            Grid.HeaderCell join = targetTypeHeader.join(propertyId.toArray());
            join.setText(targetType.getName());
        }

        for (ContourType contourType : ContourType.values()) {
            List<Object> propertyId = columns.stream()
                    .filter(column -> ((String) column.getPropertyId()).contains(contourType.toString()))
                    .map(Grid.Column::getPropertyId)
                    .collect(Collectors.toList());
            if (propertyId.size() % 3 != 0) throw new RuntimeException("Несовпадение размера ячеек");
            for (int i = 0; i < propertyId.size(); i += 3) {
                Grid.HeaderCell join = structureTypeHeader.join(propertyId.get(i), propertyId.get(i + 1), propertyId.get(i + 2));
                join.setText(contourType.getName());
            }
        }

        setSizeFull();
    }

    public void deleteSelectedRecords() {

    }

    public void readData(NbcPatients selectedPatient) {

    }

    public List<String> getFilters() {
        List<String> filter = new ArrayList<>();
        for (TargetType targetType : TargetType.values()) {
            filter.add(targetType.getName());
        }
        for (ContourType contourType : ContourType.values()) {
            filter.add(contourType.getName());
        }
        return filter;
    }

    public void updateVisibility(Set value) {
        
    }

    public enum MainInfo {
        VOLUME("Объем"), MIN30("30 минут"), MIN60("60 минут");

        private final String name;

        MainInfo(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum TargetType {
        HIZ("Хороидальное сплетение"), TARGET("Опухоль"), HYP("Гипофиз");

        private final String name;

        TargetType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum ContourType {
        SPHERE("Сфера"), ISOLYNE10("Изолиния 10"), ISOLYNE25("Изолиния 25");

        private final String name;

        ContourType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
