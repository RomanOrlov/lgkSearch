package lgk.nsbc.backend.search.dbsearch;

import lgk.nsbc.backend.Target;
import com.vaadin.ui.*;
import org.jooq.Condition;
import org.jooq.TableField;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

public class CriteriaText extends Criteria {
    class TextComponents {
        HorizontalLayout horizontalLayout;
        CheckBox checkBox;
        TextField textField;

        public TextComponents(HorizontalLayout horizontalLayout, CheckBox checkBox, TextField textField) {
            this.checkBox = checkBox;
            this.horizontalLayout = horizontalLayout;
            this.textField = textField;
        }
    }

    private ArrayList<TextComponents> textComponents = new ArrayList<>();

    public CriteriaText(SelectColumn selectColumn, Target target, String value) {
        super(selectColumn, target);
        parseCriteriaStringValue(value);
    }

    public CriteriaText(SelectColumn selectColumn, Target target) {
        this(selectColumn, target, "");
    }

    @Override
    public Condition getCondition() {
        TableField tableField = getTableField();
        Condition condition = isNullCheckBox.getValue() ? tableField.isNull() : tableField.isNotNull();
        for (TextComponents text : textComponents) {
            String textValue = text.textField.getValue();
            if (textValue != null && !textValue.isEmpty()) {
                condition = condition.or(tableField.likeIgnoreCase("%" + text.textField.getValue() + "%"));
            }
        }
        return condition;
    }

    @Override
    protected void addNewCriteria() {
        TextField textField = new TextField("Значение:");
        CheckBox checkBox = new CheckBox();
        HorizontalLayout horizontalLayout = new HorizontalLayout(checkBox,textField);
        horizontalLayout.setComponentAlignment(checkBox,Alignment.MIDDLE_CENTER);
        horizontalLayout.setSpacing(true);
        textComponents.add(new TextComponents(horizontalLayout,checkBox,textField));
        criteriaComponents.addComponent(horizontalLayout);
    }

    @Override
    protected void removeCriteria() {
        for (Iterator<TextComponents> iterator = textComponents.iterator(); iterator.hasNext();) {
            TextComponents components = iterator.next();
            if (components.checkBox.getValue()) {
                iterator.remove();
                criteriaComponents.removeComponent(components.horizontalLayout);
            }
        }
    }

    @Override
    public void parseCriteriaStringValue(String string) {
        if (!string.isEmpty()) {
            StringTokenizer tokenizer = new StringTokenizer(string,"|");
            isNullCheckBox.setValue(Boolean.getBoolean(tokenizer.nextToken()));
            while (tokenizer.hasMoreElements()) {
                int newCritetiaIndex = textComponents.size();
                String text = tokenizer.nextToken();
                addNewCriteria();
                textComponents.get(newCritetiaIndex).textField.setValue(text);
            }
        }
    }

    @Override
    public String getCriteriaStringValue() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(isNullCheckBox.getValue());
        textComponents.stream().filter(components -> components.textField.getValue() != null).forEach(components -> {
            stringBuilder.append("|");
            stringBuilder.append(components.textField.getValue());
        });
        return stringBuilder.toString();
    }
}
