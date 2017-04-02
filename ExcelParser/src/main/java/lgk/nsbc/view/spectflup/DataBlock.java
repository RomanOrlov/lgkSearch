package lgk.nsbc.view.spectflup;

import com.vaadin.data.Binder;
import com.vaadin.ui.HorizontalLayout;
import lgk.nsbc.model.spect.TargetType;
import lgk.nsbc.view.spectflup.bind.SpectFlupData;

import static lgk.nsbc.model.spect.ContourType.*;

/**
 * DataUnit для сферы и всех изолиний
 */
class DataBlock extends HorizontalLayout {

    public DataBlock(Binder<SpectFlupData> bind, TargetType targetType) {
        DataUnit sphere = new DataUnit(bind, SPHERE, targetType);
        DataUnit isolyne10 = new DataUnit(bind, ISOLYNE10, targetType);
        DataUnit isolyne25 = new DataUnit(bind, ISOLYNE25, targetType);
        setWidth("100%");
        addComponents(sphere, isolyne10, isolyne25);
    }

    public DataBlock(Binder<SpectFlupData> bind, TargetType targetType, String propertPrefix) {
        DataUnit sphere = new DataUnit(bind, SPHERE, targetType, propertPrefix);
        DataUnit isolyne10 = new DataUnit(bind, ISOLYNE10, targetType, propertPrefix);
        DataUnit isolyne25 = new DataUnit(bind, ISOLYNE25, targetType, propertPrefix);
        setWidth("100%");
        addComponents(sphere, isolyne10, isolyne25);
    }
}
