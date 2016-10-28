package lgk.nsbc.backend.search.dbsearch;

import lgk.nsbc.backend.Target;
import lgk.nsbc.backend.search.SelectableInfo;
import com.vaadin.ui.*;
import org.jooq.Condition;
import org.jooq.TableField;

public abstract class Criteria implements SelectableInfo {
    private Target target;
    private SelectColumn selectColumn;

    private Label label = new Label();
    protected CheckBox isNullCheckBox = new CheckBox("может быть NULL", true);
    private Button addCriteria = new Button("Добавить условие ИЛИ");
    private Button removeCriteria = new Button("Удалить выбранные критерии");
    private HorizontalLayout controlButtons = new HorizontalLayout(isNullCheckBox, addCriteria, removeCriteria);

    protected VerticalLayout criteriaComponents = new VerticalLayout();
    private Panel panel = new Panel(criteriaComponents);

    private VerticalLayout formLayoutContent = new VerticalLayout(label, controlButtons, panel);

    public Criteria(SelectColumn selectColumn, Target target) {
        if (selectColumn == null) {
            throw new IllegalArgumentException("Ошибка в ссылке на SelectColumn");
        }
        this.selectColumn = selectColumn;
        this.target = target;
        label.setValue(selectColumn.getRusName());
        panel.setHeight("200px");
        criteriaComponents.setHeightUndefined();
        addCriteria.addClickListener(clickEvent -> addNewCriteria());
        removeCriteria.addClickListener(clickEvent -> removeCriteria());
        controlButtons.setSpacing(true);
        controlButtons.setSizeUndefined();
        controlButtons.setComponentAlignment(isNullCheckBox, Alignment.MIDDLE_CENTER);
        criteriaComponents.setSpacing(true);
    }

    public boolean isSelected() {
        return selectColumn.isSelected();
    }

    public void setSelected(boolean selected) {
        selectColumn.setSelected(selected);
    }

    public TableField getTableField() {
        return selectColumn.getTableField();
    }

    public final void viewCriteriaEditField(FormLayout formLayout) {
        formLayout.removeAllComponents();
        formLayout.addComponent(formLayoutContent);
        formLayout.addComponent(panel);
    }

    public Target getTarget() {
        return target;
    }

    @Override
    public int getUniqueNum() {
        return selectColumn.getUniqueNum();
    }

    /**
     * Определяет условие для Where
     *
     * @return Класс jooQ Condition
     */
    public abstract Condition getCondition();

    /**
     * Добавляет в форму новое поле для еще одного ИЛИ критерия
     */
    protected abstract void addNewCriteria();

    /**
     * Удаляет выбранные критерии
     */
    protected abstract void removeCriteria();

    /**
     * Парсинг строки, вытянутой из базы. Для конкретного вида критерия парсинг немного отличается.
     *
     * @param string значение критерия, вытянутого из базы.
     */
    public abstract void parseCriteriaStringValue(String string);

    /**
     * Закатываю критерий в строку
     *
     * @return Значения критерия в строчке, которое будет хранится в базе.
     */
    public abstract String getCriteriaStringValue();

    @Override
    public long getN() {
        return selectColumn.getN();
    }

    @Override
    public void setN(long n) {
        selectColumn.setN(n);
    }

    public String getRusName() {
        String criteriaValue = getCriteriaStringValue();
        return selectColumn.getRusName() + " " + (criteriaValue == null ? "" : criteriaValue);
    }
}