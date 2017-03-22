package lgk.nsbc.view.spectflup;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import lgk.nsbc.template.model.NbcFlupSpectData;
import lgk.nsbc.template.model.spect.ContourType;
import lgk.nsbc.util.DoubleTextField;

import static lgk.nsbc.template.model.spect.MainInfo.*;

/**
 * Данные об объеме, ранней и поздней фазы
 */
class DataUnit extends HorizontalLayout {
    private final TextField volume = new DoubleTextField(VOLUME.getName());
    private final TextField earlyPhase = new DoubleTextField(MIN30.getName());
    private final TextField latePhase = new DoubleTextField(MIN60.getName());

    public DataUnit(String caption) {
        setSizeFull();
        setCaption(caption);
        addComponents(volume, earlyPhase, latePhase);
        setSpacing(true);
    }

    public NbcFlupSpectData getSpectData() {
        return NbcFlupSpectData.builder()
                .volume(Double.class.cast(volume.getConvertedValue()))
                .early_phase(Double.class.cast(earlyPhase.getConvertedValue()))
                .late_phase(Double.class.cast(latePhase.getConvertedValue()))
                .build();
    }

    public void setSpectData(NbcFlupSpectData hypData) {
        volume.setConvertedValue(hypData.getVolume());
        earlyPhase.setConvertedValue(hypData.getEarly_phase());
        latePhase.setConvertedValue(hypData.getLate_phase());
    }
}
