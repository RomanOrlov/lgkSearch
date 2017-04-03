package lgk.nsbc.view.spectflup.bind;

import com.vaadin.data.PropertyDefinition;
import com.vaadin.data.PropertySet;
import com.vaadin.data.ValueProvider;
import com.vaadin.server.Setter;

import java.util.Optional;

public class SpectDataPropertyDefinition<V> implements PropertyDefinition<SpectFlupData, V> {
    private final SpectDataPropertySet spectDataPropertySet;
    private final String propertyName;
    private final Class<V> vClass;
    private final String propertyCaption;

    public SpectDataPropertyDefinition(SpectDataPropertySet spectDataPropertySet,
                                       String propertyName,
                                       Class<V> vClass,
                                       String propertyCaption) {
        this.spectDataPropertySet = spectDataPropertySet;
        this.propertyName = propertyName;
        this.vClass = vClass;
        this.propertyCaption = propertyCaption;
    }

    @Override
    public ValueProvider<SpectFlupData, V> getGetter() {
        return (spectFlupData -> {
            Object propertyValue = spectFlupData.getValueByPropertyName(propertyName);
            return vClass.cast(propertyValue);
        });
    }

    @Override
    public Optional<Setter<SpectFlupData, V>> getSetter() {
        return Optional.of((spectFlupData, v) -> spectFlupData.setValueByPropertyName(propertyName, v));
    }

    @Override
    public Class<V> getType() {
        return vClass;
    }

    @Override
    public String getName() {
        return propertyName;
    }

    @Override
    public String getCaption() {
        return propertyCaption;
    }

    @Override
    public PropertySet<SpectFlupData> getPropertySet() {
        return spectDataPropertySet;
    }
}
