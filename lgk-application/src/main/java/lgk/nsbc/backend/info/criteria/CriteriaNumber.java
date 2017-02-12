package lgk.nsbc.backend.info.criteria;

import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import lgk.nsbc.backend.info.DisplayedInfo;
import org.jooq.Condition;
import org.jooq.TableField;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Критерий для поиска целочичленных параметров, - типа номера истории болезни, количества
 * процедур, или количества чего бы то ни было
 * Created by Роман on 18.05.2016.
 */
public class CriteriaNumber extends Criteria {
    private List<NumberComponents> numberComponents = new ArrayList<>();

    public CriteriaNumber(DisplayedInfo displayedInfo, String value) {
        super(displayedInfo, value);
    }

    public CriteriaNumber(DisplayedInfo displayedInfo) {
        this(displayedInfo, "");
    }

    @Override
    public Condition getCondition() {
        TableField tableField = getTableField();
        Condition condition = criteriaForm.isNull() ? tableField.isNull() : tableField.isNotNull();
        for (NumberComponents numberComponent : numberComponents) {
            String to = numberComponent.to.getValue();
            String from = numberComponent.from.getValue();
            if (to == null && from != null) {
                condition = condition.or(tableField.greaterOrEqual(from));
            } else if (to != null) {
                condition = condition.or(from == null ? tableField.lessOrEqual(to) : tableField.between(from, to));
            }
        }
        return condition;
    }

    @Override
    public void parseCriteriaStringValue(String string) {
        if (!string.isEmpty()) {
            StringTokenizer tokenizer = new StringTokenizer(string, "|&");
            criteriaForm.setIsNull(Boolean.getBoolean(tokenizer.nextToken()));
            while (tokenizer.hasMoreElements()) {
                String from = tokenizer.nextToken();
                String to = tokenizer.nextToken();
                NumberComponents components = new NumberComponents();
                components.from.setValue(from);
                components.to.setValue(to);
                numberComponents.add(components);
            }
        }
    }

    @Override
    public String getCriteriaStringValue() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(criteriaForm.isNull());
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

    private class NumberComponents extends HorizontalLayout implements CriteriaForm.SelectableCheckBox {
        CheckBox checkBox;
        TextField from;
        TextField to;

        NumberComponents() {
            TextField from = new TextField("От");
            from.addValidator(new IntegerRangeValidator("Введите целое число ", 0, Integer.MAX_VALUE));
            TextField to = new TextField("До");
            from.addValidator(new IntegerRangeValidator("Введите целое число ", 0, Integer.MAX_VALUE));
            addComponents(new CheckBox(), from, to);
        }

        @Override
        public boolean checkBoxSelected() {
            return checkBox.getValue();
        }
    }
}
