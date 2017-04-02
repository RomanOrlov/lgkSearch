package lgk.nsbc.view.spectflup.bind;

import com.vaadin.data.PropertyDefinition;
import com.vaadin.data.PropertySet;

import java.util.Optional;
import java.util.stream.Stream;

public class SpectDataPropertySet implements PropertySet<SpectFlupData> {
    private final static int MAX_TARGETS = 4;
    private final SpectFlupData spectFlupData;

    public SpectDataPropertySet(SpectFlupData spectFlupData) {
        this.spectFlupData = spectFlupData;
    }

    @Override
    public Stream<PropertyDefinition<SpectFlupData, ?>> getProperties() {
        return null;
    }

    @Override
    public Optional<PropertyDefinition<SpectFlupData, ?>> getProperty(String name) {

    }
}
