package lgk.nsbc.backend.info.criteria;

import com.vaadin.ui.*;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

public class CriteriaForm<T extends HorizontalLayout & CriteriaForm.SelectableCheckBox> extends VerticalLayout {
    private CheckBox isNullCheckBox = new CheckBox("Может отсутствовать ", false);
    private VerticalLayout criteriaComponents = new VerticalLayout();
    private List<T> criteria;

    public CriteriaForm(List<T> criteria, Supplier<T> criteriaGenerator, String name) {
        this.criteria = criteria;
        Button addCriteria = new Button("Добавить критерий");
        Button removeCriteria = new Button("Удалить выбранные критерии");
        HorizontalLayout controlButtons = new HorizontalLayout(isNullCheckBox, addCriteria, removeCriteria);
        Panel panel = new Panel(criteriaComponents);
        panel.setHeight("200px");
        addComponents(new Label(name), controlButtons, panel);
        criteriaComponents.setHeightUndefined();
        criteriaComponents.setSpacing(true);
        criteria.forEach(criteriaComponents::addComponent);
        addCriteria.addClickListener(clickEvent -> addNewCriteria(criteriaGenerator.get()));
        removeCriteria.addClickListener(clickEvent -> removeCriteria());
        controlButtons.setSpacing(true);
        controlButtons.setSizeUndefined();
        controlButtons.setComponentAlignment(isNullCheckBox, Alignment.MIDDLE_CENTER);
    }

    public void addNewCriteria(T t) {
        t.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        t.setSpacing(true);
        criteriaComponents.addComponent(t);
        criteria.add(t);
    }

    private void removeCriteria() {
        Iterator<T> iterator = criteria.iterator();
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (t.checkBoxSelected()) {
                criteriaComponents.removeComponent(t);
                iterator.remove();
            }
        }
    }

    interface SelectableCheckBox {
        boolean checkBoxSelected();
    }

    public void setIsNull(boolean isNull) {
        isNullCheckBox.setValue(isNull);
    }

    public boolean isNull() {
        return isNullCheckBox.getValue();
    }
}