package lgk.nsbc.backend.search.dbsearch;

import lgk.nsbc.backend.Target;
import com.vaadin.ui.*;
import org.jooq.Condition;
import org.jooq.TableField;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

public class CriteriaDate extends Criteria {
    /**
     * Тупая структура данных, - использование вместо этого кучи листов неудобно.
     */
    class DateComponents {
        HorizontalLayout horizontalLayout;
        CheckBox checkBox;
        DateField from;
        DateField to;

        public DateComponents(HorizontalLayout horizontalLayout, CheckBox checkBox, DateField from, DateField to) {
            this.checkBox = checkBox;
            this.from = from;
            this.horizontalLayout = horizontalLayout;
            this.to = to;
        }
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.mm.yyyy");
    private ArrayList<DateComponents> dateComponents = new ArrayList<>();

    public CriteriaDate(SelectColumn selectColumn, Target target) {
        this(selectColumn,target,"");
    }

    public CriteriaDate(SelectColumn selectColumn, Target target, String value) {
        super(selectColumn,target);
        parseCriteriaStringValue(value);
    }

    @Override
    public Condition getCondition() {
        TableField tableField = getTableField();
        Condition condition = isNullCheckBox.getValue()?tableField.isNull():tableField.isNotNull();
        for (DateComponents dateComponent : dateComponents) {
            Date to = dateComponent.to.getValue();
            Date from = dateComponent.from.getValue();
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
        DateField dateFieldFrom = new DateField("От");
        DateField dateFieldTo = new DateField("До");
        CheckBox checkBox = new CheckBox();
        HorizontalLayout dates = new HorizontalLayout(checkBox,dateFieldFrom,dateFieldTo);
        dates.setComponentAlignment(checkBox,Alignment.MIDDLE_CENTER);
        dates.setSpacing(true);
        dateComponents.add(new DateComponents(dates,checkBox,dateFieldFrom,dateFieldTo));
        criteriaComponents.addComponent(dates);
    }

    @Override
    protected void removeCriteria() {
        for (Iterator<DateComponents> iterator = dateComponents.iterator();iterator.hasNext();) {
            DateComponents components = iterator.next();
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
                int newCritetiaIndex = dateComponents.size();
                addNewCriteria();
                Date fromDate;
                Date toDate;
                try {
                    fromDate = DATE_FORMAT.parse(from);
                } catch (ParseException ex) {
                    fromDate = null;
                }
                try {
                    toDate = DATE_FORMAT.parse(to);
                } catch (ParseException ex) {
                    toDate = null;
                }
                dateComponents.get(newCritetiaIndex).from.setValue(fromDate);
                dateComponents.get(newCritetiaIndex).to.setValue(toDate);
            }
        }
    }

    @Override
    public String getCriteriaStringValue() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(isNullCheckBox.getValue());
        for (DateComponents dateComponent : dateComponents) {
            Date from = dateComponent.from.getValue();
            Date to = dateComponent.to.getValue();
            if (from == null && to == null) {
                continue;
            }
            stringBuilder.append("|");
            stringBuilder.append(from == null ? "null" : DATE_FORMAT.format(from))
                    .append("&")
                    .append(to == null ? "null" : DATE_FORMAT.format(to));
        }

        return stringBuilder.toString();
    }
}
