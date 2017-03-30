package lgk.nsbc.view.spectflup;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import lgk.nsbc.template.model.NbcTarget;
import lgk.nsbc.template.model.spect.TargetType;

import java.util.List;

/**
 * Данные для мишени
 */
class TargetData extends HorizontalLayout {
    final CheckBox isSelected = new CheckBox();
    final NativeSelect<NbcTarget> selectedTarget;
    final DataBlock dataBlock;

    public TargetData(List<NbcTarget> targets, TargetType targetType) {
        dataBlock = new DataBlock(targetType);

        selectedTarget = new NativeSelect<>("Выберите мишень", targets);
        selectedTarget.setEmptySelectionAllowed(false);
        selectedTarget.setWidth("100%");
        selectedTarget.setHeight("40px");

        addComponents(isSelected, selectedTarget, dataBlock);
        setExpandRatio(dataBlock, 1);

        isSelected.setHeight("40px");
        isSelected.setWidth("20px");
    }
}
