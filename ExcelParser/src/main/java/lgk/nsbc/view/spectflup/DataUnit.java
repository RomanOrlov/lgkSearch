package lgk.nsbc.view.spectflup;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import lgk.nsbc.model.spect.ContourType;
import lgk.nsbc.model.spect.TargetType;
import lgk.nsbc.view.spectflup.bind.SpectFlupData;

import static lgk.nsbc.model.spect.MainInfo.*;

/**
 * Данные об объеме, ранней и поздней фазы
 */
class DataUnit extends HorizontalLayout {
    private TextField volume = new TextField(VOLUME.getName());
    private TextField earlyPhase = new TextField(MIN30.getName());
    private TextField latePhase = new TextField(MIN60.getName());

    public DataUnit(Binder<SpectFlupData> bind, ContourType contourType, TargetType targetType) {
        setSizeFull();
        setCaption(contourType.getName());
        addComponents(volume, earlyPhase, latePhase);

        String errorMessage = "Неправильный формат";
        String firstIdPart = targetType.toString() + contourType.toString();
        bind.forField(volume)
                .withConverter(new StringToDoubleConverter(errorMessage))
                .bind(firstIdPart + VOLUME.toString());
        bind.forField(earlyPhase)
                .withConverter(new StringToDoubleConverter(errorMessage))
                .bind(firstIdPart + MIN30.toString());
        bind.forField(latePhase)
                .withConverter(new StringToDoubleConverter(errorMessage))
                .bind(firstIdPart + MIN60.toString());
    }

    public DataUnit(Binder<SpectFlupData> bind, ContourType contourType, TargetType targetType, String propertyPrefix) {
        setSizeFull();
        setCaption(contourType.getName());
        addComponents(volume, earlyPhase, latePhase);

        String errorMessage = "Неправильный формат";
        String firstIdPart = propertyPrefix + targetType.toString() + contourType.toString();
        bind.forField(volume)
                .withConverter(new StringToDoubleConverter(errorMessage))
                .bind(firstIdPart + VOLUME.toString());
        bind.forField(earlyPhase)
                .withConverter(new StringToDoubleConverter(errorMessage))
                .bind(firstIdPart + MIN30.toString());
        bind.forField(latePhase)
                .withConverter(new StringToDoubleConverter(errorMessage))
                .bind(firstIdPart + MIN60.toString());
    }
}
