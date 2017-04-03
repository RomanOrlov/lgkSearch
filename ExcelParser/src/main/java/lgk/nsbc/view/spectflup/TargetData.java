package lgk.nsbc.view.spectflup;

import com.vaadin.data.Binder;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import lgk.nsbc.model.NbcTarget;
import lgk.nsbc.model.spect.TargetType;
import lgk.nsbc.view.spectflup.bind.SpectFlupData;

import java.util.List;

import static lgk.nsbc.model.spect.TargetType.TARGET;

/**
 * Данные для мишени
 */
class TargetData extends HorizontalLayout {
    final NativeSelect<NbcTarget> selectedTarget;
    final DataBlock dataBlock;

    public TargetData(Binder<SpectFlupData> bind, List<NbcTarget> targets, Integer order) {
        dataBlock = new DataBlock(bind, TARGET, order.toString());
        selectedTarget = new NativeSelect<>("Выберите мишень", targets);
        selectedTarget.setEmptySelectionAllowed(false);
        selectedTarget.setWidth("100%");
        selectedTarget.setHeight("40px");

        addComponents(selectedTarget, dataBlock);
        setExpandRatio(dataBlock, 1);

        bind.forField(selectedTarget).bind(order.toString() + TARGET.toString());
    }
}
