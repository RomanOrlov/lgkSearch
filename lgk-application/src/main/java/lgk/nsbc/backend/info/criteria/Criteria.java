package lgk.nsbc.backend.info.criteria;

import com.vaadin.ui.FormLayout;
import lgk.nsbc.backend.info.DisplayedInfo;
import lgk.nsbc.backend.info.InfoType;
import lgk.nsbc.backend.info.SelectableInfo;
import lgk.nsbc.backend.info.ViewableInfo;
import org.jooq.Condition;
import org.jooq.Table;
import org.jooq.TableField;

public abstract class Criteria implements SelectableInfo, ViewableInfo {
    private final DisplayedInfo displayedInfo;
    protected CriteriaForm criteriaForm;

    public Criteria(DisplayedInfo displayedInfo, String value) {
        this.displayedInfo = displayedInfo;
        parseCriteriaStringValue(value);
    }

    public Criteria(DisplayedInfo displayedInfo) {
        this(displayedInfo, "");
    }

    @Override
    public boolean isSelected() {
        return displayedInfo.isSelected();
    }

    @Override
    public void setSelected(boolean isSelected) {
        displayedInfo.setSelected(isSelected);
    }

    @Override
    public InfoType getInfoType() {
        return displayedInfo.getInfoType();
    }

    @Override
    public TableField getTableField() {
        return displayedInfo.getTableField();
    }

    /**
     * Определяет условие для Where
     *
     * @return Класс jooQ Condition
     */
    public abstract Condition getCondition();

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

    public final void viewCriteriaEditField(FormLayout formLayout) {
        formLayout.removeAllComponents();
        formLayout.addComponent(criteriaForm);
    }

    @Override
    public String toString() {
        return getRusName();
    }

    @Override
    public String getRusName() {
        return displayedInfo.getRusName();
    }

    @Override
    public Table getTable() {
        return displayedInfo.getTable();
    }
}
