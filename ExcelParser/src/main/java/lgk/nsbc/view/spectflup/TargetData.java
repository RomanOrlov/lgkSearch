package lgk.nsbc.view.spectflup;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import lgk.nsbc.template.model.NbcTarget;
import lgk.nsbc.template.model.spect.TargetType;

import java.util.List;

/**
 * Данные для мишени
 */
class TargetData extends DataBlock {
    private final CheckBox isSelected = new CheckBox();
    private final ComboBox selectedTarget;

    public TargetData(List<NbcTarget> targets, TargetType targetType) {
        super(targetType);
        HorizontalLayout components = new HorizontalLayout();
        selectedTarget = new ComboBox("Выберите мишень", targets);
        selectedTarget.setRequired(true);
        selectedTarget.setNullSelectionAllowed(false);
        selectedTarget.setWidth("170px");
        selectedTarget.setHeight("40px");
        components.addComponents(isSelected, selectedTarget);
        addComponent(components, 0);
        components.setComponentAlignment(isSelected, Alignment.BOTTOM_LEFT);
        components.setComponentAlignment(selectedTarget, Alignment.BOTTOM_LEFT);
        components.setSpacing(true);
        isSelected.setHeight("40px");
        isSelected.setWidth("20px");
        MarginInfo margin = new MarginInfo(true, false, true, false);
        setMargin(margin);
        setExpandRatio(components, 0.9f);
    }

    public boolean isRowSelected() {
        return isSelected.getValue();
    }

    public boolean isEmpty() {
        return selectedTarget.isEmpty();
    }

    public Long getSelectedTargetN() {
        NbcTarget nbcTarget = (NbcTarget) selectedTarget.getValue();
        return nbcTarget.getN();
    }

    public void setSelectedTarfet(Object newValue) {
        selectedTarget.setValue(newValue);
    }
}
