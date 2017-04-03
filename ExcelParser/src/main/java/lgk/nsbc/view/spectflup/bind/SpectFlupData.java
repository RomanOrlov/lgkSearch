package lgk.nsbc.view.spectflup.bind;

import lgk.nsbc.model.NbcTarget;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс для Binding с компонентами окна редактирования данных ОФЕКТ
 */
@Getter
@Setter
public class SpectFlupData {
    private Double dose;
    private LocalDate studyDate;
    private Map<String, Double> volumeAndPhases = new HashMap<>();
    private Map<String, NbcTarget> selectedTargets = new HashMap<>();

    public Object getValueByPropertyName(String propertyName) {
        if (volumeAndPhases.containsKey(propertyName)) return volumeAndPhases.get(propertyName);
        if (selectedTargets.containsKey(propertyName)) return selectedTargets.get(propertyName);
        if (propertyName.equals("dose")) return dose;
        if (propertyName.equals("studyDate")) return studyDate;
        throw new RuntimeException("No such property");
    }

    public void setValueByPropertyName(String propertyName, Object value) {
        if (volumeAndPhases.containsKey(propertyName)) volumeAndPhases.put(propertyName, (Double) value);
        if (selectedTargets.containsKey(propertyName)) selectedTargets.put(propertyName, (NbcTarget) value);
        if (propertyName.equals("dose")) dose = (Double) value;
        if (propertyName.equals("studyDate")) studyDate = (LocalDate) value;
    }
}
