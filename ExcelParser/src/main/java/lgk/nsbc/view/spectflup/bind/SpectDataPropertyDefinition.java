package lgk.nsbc.view.spectflup.bind;

import com.vaadin.data.PropertyDefinition;
import com.vaadin.data.PropertySet;
import com.vaadin.data.ValueProvider;
import com.vaadin.server.Setter;

import java.util.Optional;

public class SpectDataPropertyDefinition<V> implements PropertyDefinition<SpectFlupData, V> {
    private final SpectDataPropertySet spectDataPropertySet;

    public SpectDataPropertyDefinition(SpectDataPropertySet spectDataPropertySet) {
        this.spectDataPropertySet = spectDataPropertySet;
    }

    @Override
    public ValueProvider<SpectFlupData, V> getGetter() {
        return spectFlupData -> spectFlupData.getProperties().get("");
    }

    @Override
    public Optional<Setter<SpectFlupData, V>> getSetter() {
        return null;
    }

    @Override
    public Class<V> getType() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getCaption() {
        return null;
    }

    @Override
    public PropertySet<SpectFlupData> getPropertySet() {
        return spectDataPropertySet;
    }
}
