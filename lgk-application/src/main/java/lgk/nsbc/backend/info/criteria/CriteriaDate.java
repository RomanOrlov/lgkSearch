package lgk.nsbc.backend.info.criteria;

import com.vaadin.ui.CheckBox;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import lgk.nsbc.backend.info.DisplayedInfo;
import org.jooq.Condition;
import org.jooq.TableField;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class CriteriaDate extends Criteria {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.mm.yyyy");
    private List<DateComponents> dateComponents = new ArrayList<>();

    public CriteriaDate(DisplayedInfo displayedInfo) {
        this(displayedInfo, "");
    }

    public CriteriaDate(DisplayedInfo displayedInfo, String value) {
        super(displayedInfo, value);
        criteriaForm = new CriteriaForm<>(dateComponents, DateComponents::new, displayedInfo.getRusName());
    }

    @Override
    public Condition getCondition() {
        TableField tableField = getTableField();
        Condition condition = criteriaForm.isNull() ? tableField.isNull() : tableField.isNotNull();
        for (DateComponents dateComponent : dateComponents) {
            Date to = dateComponent.to.getValue();
            Date from = dateComponent.from.getValue();
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
                DateComponents components = new DateComponents();
                components.from.setValue(getDate(from));
                components.to.setValue(getDate(to));
                dateComponents.add(components);
            }
        }
    }

    private Date getDate(String to) {
        try {
            return DATE_FORMAT.parse(to);
        } catch (ParseException ex) {
            return null;
        }
    }

    private String getDateString(Date to) {
        return to == null ? "null" : DATE_FORMAT.format(to);
    }

    @Override
    public String getCriteriaStringValue() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(criteriaForm.isNull());
        for (DateComponents dateComponent : dateComponents) {
            Date from = dateComponent.from.getValue();
            Date to = dateComponent.to.getValue();
            if (from == null && to == null) {
                continue;
            }
            stringBuilder.append("|");
            stringBuilder.append(getDateString(from))
                    .append("&")
                    .append(getDateString(to));
        }
        return stringBuilder.toString();
    }

    private class DateComponents extends HorizontalLayout implements CriteriaForm.SelectableCheckBox {
        CheckBox checkBox;
        DateField from;
        DateField to;

        DateComponents() {
            addComponents(new CheckBox(), new DateField("От"), new DateField("До"));
        }

        @Override
        public boolean checkBoxSelected() {
            return checkBox.getValue();
        }
    }
}
