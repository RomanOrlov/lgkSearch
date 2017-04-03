package lgk.nsbc.view.spectflup.bind;

import com.vaadin.data.PropertyDefinition;
import com.vaadin.data.PropertySet;
import lgk.nsbc.model.NbcTarget;
import lgk.nsbc.model.spect.ContourType;
import lgk.nsbc.model.spect.MainInfo;
import lombok.Getter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static lgk.nsbc.model.spect.ContourType.SPHERE;
import static lgk.nsbc.model.spect.TargetType.*;

public class SpectDataPropertySet implements PropertySet<SpectFlupData> {
    public static final Integer MAX_TARGETS = 4;
    @Getter
    private SpectFlupData spectData;
    private final Map<String, PropertyDefinition<SpectFlupData, ?>> propertyDefinitions = new HashMap<>();

    public SpectDataPropertySet() {
        spectData = new SpectFlupData();
        String dosePropertyName = "dose";
        SpectDataPropertyDefinition<Double> dose = new SpectDataPropertyDefinition<>(this,
                dosePropertyName,
                Double.class,
                "Доза");
        propertyDefinitions.put(dosePropertyName, dose);
        String studyDatePropertyName = "studyDate";
        SpectDataPropertyDefinition<LocalDate> studyDate = new SpectDataPropertyDefinition<>(this,
                studyDatePropertyName,
                LocalDate.class,
                "Дата исследования");
        propertyDefinitions.put(studyDatePropertyName, studyDate);
        // Гипофиз
        for (MainInfo mainInfo : MainInfo.values()) {
            String propertyName = HYP.toString() + SPHERE.toString() + mainInfo.toString();
            SpectDataPropertyDefinition<Double> hypProperty = new SpectDataPropertyDefinition<>(this,
                    propertyName,
                    Double.class,
                    mainInfo.getName());
            propertyDefinitions.put(propertyName, hypProperty);
            spectData.getVolumeAndPhases().put(propertyName, null);
        }
        // Хор. сплетение
        for (ContourType contourType : ContourType.values()) {
            for (MainInfo mainInfo : MainInfo.values()) {
                String propertyName = HIZ.toString() + contourType.toString() + mainInfo.toString();
                SpectDataPropertyDefinition<Double> hizProperty = new SpectDataPropertyDefinition<>(this,
                        propertyName,
                        Double.class,
                        mainInfo.getName());
                propertyDefinitions.put(propertyName, hizProperty);
                spectData.getVolumeAndPhases().put(propertyName, null);
            }
        }

        // Мишени
        for (int i = 0; i < MAX_TARGETS; i++) {
            for (ContourType contourType : ContourType.values()) {
                for (MainInfo mainInfo : MainInfo.values()) {
                    String propertyName = String.valueOf(i) + TARGET.toString() + contourType.toString() + mainInfo.toString();
                    SpectDataPropertyDefinition<Double> targetProperty = new SpectDataPropertyDefinition<>(this,
                            propertyName,
                            Double.class,
                            mainInfo.getName());
                    propertyDefinitions.put(propertyName, targetProperty);
                    spectData.getVolumeAndPhases().put(propertyName, null);
                }
            }
            // Проперти выбранной мишени
            String propertyName = String.valueOf(i) + TARGET.toString();
            SpectDataPropertyDefinition<NbcTarget> targetProperty = new SpectDataPropertyDefinition<>(this,
                    propertyName,
                    NbcTarget.class,
                    "Мишень");
            propertyDefinitions.put(propertyName, targetProperty);
            spectData.getSelectedTargets().put(propertyName, null);
        }
    }

    @Override
    public Stream<PropertyDefinition<SpectFlupData, ?>> getProperties() {
        return propertyDefinitions.values().stream();
    }

    @Override
    public Optional<PropertyDefinition<SpectFlupData, ?>> getProperty(String name) {
        if (propertyDefinitions.containsKey(name))
            return Optional.of(propertyDefinitions.get(name));
        return Optional.empty();
    }
}
