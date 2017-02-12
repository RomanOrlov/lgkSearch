package lgk.nsbc.backend.info.criteria;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import lgk.nsbc.backend.info.DisplayedInfo;
import org.jooq.Condition;
import org.jooq.TableField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class CriteriaList extends Criteria {
    private final Map<String, Long> options;
    private TableField joinTableField;
    private List<ListComponents> listComponents = new ArrayList<>();

    public CriteriaList(DisplayedInfo displayedInfo,
                        Map<String, Long> options,
                        TableField joinTableField) {
        this(displayedInfo, options, joinTableField, "");
    }

    public CriteriaList(DisplayedInfo displayedInfo,
                        Map<String, Long> options,
                        TableField joinTableField,
                        String value) {
        super(displayedInfo, value);
        this.joinTableField = joinTableField;
        this.options = options;
        criteriaForm = new CriteriaForm<>(listComponents, ListComponents::new, displayedInfo.getRusName());
    }

    @Override
    public Condition getCondition() {
        Condition condition = criteriaForm.isNull() ? joinTableField.isNull() : joinTableField.isNotNull();
        for (ListComponents components : listComponents) {
            Object selectedValue = components.nativeSelect.getValue();
            if (selectedValue != null) {
                condition = condition.or(joinTableField.eq(options.get(selectedValue)));
            }
        }
        return condition;
    }

    @Override
    public void parseCriteriaStringValue(String string) {
        if (!string.isEmpty()) {
            StringTokenizer tokenizer = new StringTokenizer(string, "|");
            String nextTokenString = tokenizer.nextToken();
            criteriaForm.setIsNull(Boolean.getBoolean(nextTokenString));
            while (tokenizer.hasMoreElements()) {
                int optionIndex = Integer.parseInt(tokenizer.nextToken());
                String value = null;
                for (Map.Entry<String, Long> in : options.entrySet()) {
                    if (in.getValue() == optionIndex) {
                        value = in.getKey();
                        break;
                    }
                }
                ListComponents components = new ListComponents();
                components.nativeSelect.setValue(value);
                listComponents.add(components);
            }
        }
    }

    @Override
    public String getCriteriaStringValue() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(criteriaForm.isNull());
        listComponents.stream().filter(components -> components.nativeSelect.getValue() != null).forEach(components -> {
            stringBuilder.append("|");
            stringBuilder.append(options.get(components.nativeSelect.getValue()));
        });
        return stringBuilder.toString();
    }


    private class ListComponents extends HorizontalLayout implements CriteriaForm.SelectableCheckBox {
        CheckBox checkBox;
        NativeSelect nativeSelect;

        ListComponents() {
            NativeSelect nativeSelect = new NativeSelect("Выберите", options.keySet());
            this.nativeSelect.setWidth("100%");
            this.nativeSelect.setNullSelectionAllowed(false);
            addComponents(new CheckBox(), nativeSelect);
        }

        @Override
        public boolean checkBoxSelected() {
            return checkBox.getValue();
        }
    }
}