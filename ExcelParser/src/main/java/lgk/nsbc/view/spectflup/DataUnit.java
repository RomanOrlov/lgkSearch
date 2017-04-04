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
    private TargetType targetType;

    public DataUnit(Binder<SpectFlupData> bind, ContourType contourType, TargetType targetType) {
        this.targetType = targetType;
        init(contourType);
        String firstIdPart = targetType.toString() + contourType.toString();
        bind(bind, firstIdPart);
    }

    public DataUnit(Binder<SpectFlupData> bind, ContourType contourType, TargetType targetType, String propertyPrefix) {
        this.targetType = targetType;
        init(contourType);
        String firstIdPart = propertyPrefix + targetType.toString() + contourType.toString();
        bind(bind, firstIdPart);

    }

    private void bind(Binder<SpectFlupData> bind, String firstIdPart) {
        String errorMessage = "Неправильный формат";
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

    private void init(ContourType contourType) {
        setSizeFull();
        addComponents(volume, earlyPhase, latePhase);
        if (targetType != TargetType.HYP) {
            setCaption(contourType.getName());
            volume.setWidth("100px");
            earlyPhase.setWidth("100px");
            latePhase.setWidth("100px");
        }
        else {
            volume.setWidth("100%");
            earlyPhase.setWidth("100%");
            latePhase.setWidth("100%");
            setExpandRatio(volume, 1);
            setExpandRatio(earlyPhase, 1);
            setExpandRatio(latePhase, 1);
        }
    }
}
