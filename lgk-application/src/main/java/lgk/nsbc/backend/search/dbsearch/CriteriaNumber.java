package lgk.nsbc.backend.search.dbsearch;

import lgk.nsbc.backend.Target;
import com.vaadin.ui.*;
import org.jooq.Condition;
import org.jooq.TableField;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * Критерий для поиска целочичленных параметров, - типа номера истории болезни, количества
 * процедур, или количества чего бы то ни было
 * Created by Роман on 18.05.2016.
 */
public class CriteriaNumber extends Criteria {

    class NumberComponents {
        HorizontalLayout horizontalLayout;
        CheckBox checkBox;
        TextField from;
        TextField to;

        public NumberComponents(HorizontalLayout horizontalLayout, CheckBox checkBox, TextField from, TextField to) {
            this.checkBox = checkBox;
            this.from = from;
            this.horizontalLayout = horizontalLayout;
            this.to = to;
        }
    }

    ArrayList<NumberComponents> numberComponents = new ArrayList<>();

    public CriteriaNumber(SelectColumn selectColumn, Target target,String value) {
        super(selectColumn,target);
        parseCriteriaStringValue(value);
    }

    public CriteriaNumber(SelectColumn selectColumn, Target target) {
        this(selectColumn,target,"");
    }

    @Override
    public Condition getCondition() {
        TableField tableField = getTableField();
        Condition condition = isNullCheckBox.getValue()?tableField.isNull():tableField.isNotNull();
        for (NumberComponents numberComponent : numberComponents) {
            String to = numberComponent.to.getValue();
            String from = numberComponent.from.getValue();
            if (to == null&&from != null) {
                condition = condition.or(tableField.greaterOrEqual(from));
            } else if (to!=null) {
                condition = condition.or(from == null ? tableField.lessOrEqual(to) : tableField.between(from, to));
            }
        }
        return condition;
    }

    @Override
    protected void addNewCriteria() {
        TextField textFieldFrom = new TextField("От");
        TextField textFieldTo = new TextField("До");
        CheckBox checkBox = new CheckBox();
        HorizontalLayout dates = new HorizontalLayout(checkBox,textFieldFrom,textFieldTo);
        dates.setComponentAlignment(checkBox,Alignment.MIDDLE_CENTER);
        dates.setSpacing(true);
        numberComponents.add(new NumberComponents(dates,checkBox,textFieldFrom,textFieldTo));
        criteriaComponents.addComponent(dates);
    }

    @Override
    protected void removeCriteria() {
        for (Iterator<NumberComponents> iterator = numberComponents.iterator(); iterator.hasNext();) {
            NumberComponents components = iterator.next();
            if (components.checkBox.getValue()) {
                iterator.remove();
                criteriaComponents.removeComponent(components.horizontalLayout);
            }
        }
    }

    @Override
    public void parseCriteriaStringValue(String string) {
        if (!string.isEmpty()) {
            StringTokenizer tokenizer = new StringTokenizer(string,"|&");
            isNullCheckBox.setValue(Boolean.getBoolean(tokenizer.nextToken()));
            while (tokenizer.hasMoreElements()) {
                String from = tokenizer.nextToken();
                String to = tokenizer.nextToken();
                int newCritetiaIndex = numberComponents.size();
                addNewCriteria();
                numberComponents.get(newCritetiaIndex).from.setValue(from);
                numberComponents.get(newCritetiaIndex).to.setValue(to);
            }
        }
    }

    @Override
    public String getCriteriaStringValue() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(isNullCheckBox.getValue());
        for (NumberComponents dateComponent : numberComponents) {
            String from = dateComponent.from.getValue();
            String to = dateComponent.to.getValue();
            if (from == null && to == null) {
                continue;
            }
            stringBuilder.append("|");
            stringBuilder.append(from == null ? "null" : from)
                    .append("&")
                    .append(to == null ? "null" : to);
        }

        return stringBuilder.toString();
    }
}
