package lgk.nsbc.backend.info.criteria;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import lgk.nsbc.backend.info.DisplayedInfo;
import org.jooq.Condition;
import org.jooq.TableField;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CriteriaText extends Criteria {
    private List<TextComponents> textComponents = new ArrayList<>();

    public CriteriaText(DisplayedInfo displayedInfo, String value) {
        super(displayedInfo, value);
        criteriaForm = new CriteriaForm<>(textComponents, TextComponents::new, displayedInfo.getRusName());
    }

    public CriteriaText(DisplayedInfo displayedInfo) {
        this(displayedInfo, "");
    }

    @Override
    public Condition getCondition() {
        TableField tableField = getTableField();
        Condition condition = criteriaForm.isNull() ? tableField.isNull() : tableField.isNotNull();
        for (TextComponents text : textComponents) {
            String textValue = text.textField.getValue();
            if (textValue != null && !textValue.isEmpty()) {
                condition = condition.or(tableField.likeIgnoreCase("%" + text.textField.getValue() + "%"));
            }
        }
        return condition;
    }

    @Override
    public void parseCriteriaStringValue(String string) {
        if (!string.isEmpty()) {
            StringTokenizer tokenizer = new StringTokenizer(string, "|");
            criteriaForm.setIsNull(Boolean.getBoolean(tokenizer.nextToken()));
            while (tokenizer.hasMoreElements()) {
                String text = tokenizer.nextToken();
                TextComponents components = new TextComponents();
                components.textField.setValue(text);
                textComponents.add(components);
            }
        }
    }

    @Override
    public String getCriteriaStringValue() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(criteriaForm.isNull());
        textComponents.stream().filter(components -> components.textField.getValue() != null).forEach(components -> {
            stringBuilder.append("|");
            stringBuilder.append(components.textField.getValue());
        });
        return stringBuilder.toString();
    }

    private class TextComponents extends HorizontalLayout implements CriteriaForm.SelectableCheckBox {
        CheckBox checkBox;
        TextField textField;

        TextComponents() {
            addComponents(new CheckBox(), new TextField("Значение: "));
        }

        @Override
        public boolean checkBoxSelected() {
            return checkBox.getValue();
        }
    }
}
