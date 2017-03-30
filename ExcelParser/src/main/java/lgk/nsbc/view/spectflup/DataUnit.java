package lgk.nsbc.view.spectflup;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import lgk.nsbc.view.spectflup.bind.DataUnitBind;

import static lgk.nsbc.template.model.spect.MainInfo.*;

/**
 * Данные об объеме, ранней и поздней фазы
 */
class DataUnit extends HorizontalLayout {
    Binder<DataUnitBind> bind = new Binder<>(DataUnitBind.class);

    public DataUnit(String caption) {
        setSizeFull();
        setCaption(caption);
        TextField volume = new TextField(VOLUME.getName());
        TextField earlyPhase = new TextField(MIN30.getName());
        TextField latePhase = new TextField(MIN60.getName());
        addComponents(volume, earlyPhase, latePhase);

        bind.forField(volume)
                .withConverter(new StringToDoubleConverter("Неправильный формат"))
                .bind(DataUnitBind::getVolume, DataUnitBind::setVolume);
        bind.forField(earlyPhase)
                .withConverter(new StringToDoubleConverter("Неправильный формат"))
                .bind(DataUnitBind::getEarlyPhase, DataUnitBind::setEarlyPhase);
        bind.forField(latePhase)
                .withConverter(new StringToDoubleConverter("Неправильный формат"))
                .bind(DataUnitBind::getLatePhase, DataUnitBind::setLatePhase);
    }
}
