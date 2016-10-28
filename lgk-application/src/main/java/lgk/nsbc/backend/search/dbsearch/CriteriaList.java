package lgk.nsbc.backend.search.dbsearch;

import lgk.nsbc.backend.DB;
import lgk.nsbc.backend.Target;
import com.vaadin.ui.*;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CriteriaList extends Criteria {
    private final LinkedHashMap<String,Long> options;
    private TableField joinTableField;

    class ListComponents {
        HorizontalLayout horizontalLayout;
        CheckBox checkBox;
        NativeSelect nativeSelect;

        public ListComponents(HorizontalLayout horizontalLayout, CheckBox checkBox, NativeSelect nativeSelect) {
            this.checkBox = checkBox;
            this.horizontalLayout = horizontalLayout;
            this.nativeSelect = nativeSelect;
        }
    }

    private ArrayList<ListComponents> listComponents = new ArrayList<>();

    public CriteriaList(SelectColumn selectColumn,
                        Target target,
                        TableField valueTableField,
                        TableField joinTableField) {
        this(selectColumn,target,valueTableField,joinTableField,"");
    }

    public CriteriaList(SelectColumn selectColumn,
                        Target target,
                        TableField valueTableField,
                        TableField joinTableField,
                        String value) {
        super(selectColumn,target);
        this.joinTableField = joinTableField;
        this.options = getDictionaryValues(valueTableField,joinTableField);
        parseCriteriaStringValue(value);
    }

    @Override
    public Condition getCondition() {
        Condition condition = isNullCheckBox.getValue()?joinTableField.isNull():joinTableField.isNotNull();
        for (ListComponents components : listComponents) {
            Object selectedValue = components.nativeSelect.getValue();
            if (selectedValue != null) {
                condition = condition.or(joinTableField.eq(options.get(selectedValue)));
            }
        }
        return condition;
    }

    @Override
    protected void addNewCriteria() {
        NativeSelect nativeSelect = new NativeSelect("Выберите",options.keySet());
        CheckBox checkBox = new CheckBox();
        HorizontalLayout listSelect = new HorizontalLayout(checkBox,nativeSelect);
        listSelect.setComponentAlignment(checkBox,Alignment.MIDDLE_CENTER);
        listSelect.setSpacing(true);
        nativeSelect.setWidth("100%");
        nativeSelect.setNullSelectionAllowed(false);
        listComponents.add(new ListComponents(listSelect,checkBox,nativeSelect));
        criteriaComponents.addComponent(listSelect);
    }

    @Override
    protected void removeCriteria() {
        for (Iterator<ListComponents> iterator = listComponents.iterator(); iterator.hasNext();) {
            ListComponents components = iterator.next();
            if (components.checkBox.getValue()) {
                iterator.remove();
                criteriaComponents.removeComponent(components.horizontalLayout);
            }
        }
    }

    @Override
    public void parseCriteriaStringValue(String string) {
        //string = "true|2010|2020";
        if (!string.isEmpty()) {
            StringTokenizer tokenizer = new StringTokenizer(string,"|");
            String[] strings = string.split("\\p{Punct}");
            String nextTokenString = tokenizer.nextToken();
            isNullCheckBox.setValue(Boolean.getBoolean(nextTokenString));
            while (tokenizer.hasMoreElements()) {
                nextTokenString = tokenizer.nextToken();
                int newCritetiaIndex = listComponents.size();
                int optionIndex = Integer.parseInt(nextTokenString);
                addNewCriteria();
                String value = null;
                for (Map.Entry<String,Long> in : options.entrySet()) {
                    if (in.getValue()==optionIndex) {
                        value = in.getKey();
                        break;
                    }
                }
                listComponents.get(newCritetiaIndex).nativeSelect.setValue(value);
            }
        }
    }

    @Override
    public String getCriteriaStringValue() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(isNullCheckBox.getValue());
        listComponents.stream().filter(components -> components.nativeSelect.getValue() != null).forEach(components -> {
            stringBuilder.append("|");
            stringBuilder.append(options.get(components.nativeSelect.getValue()));
        });
        return stringBuilder.toString();
    }

    /**
     * @param stringName поле из словаря (Таблицы в базе), откуда будет браться название
     * @param intKey поле для join, по которому будет подсоединяться словарь.
     * @return
     */
    private LinkedHashMap<String,Long> getDictionaryValues(TableField stringName,TableField intKey) {
        LinkedHashMap<String,Long> linkedHashMap = new LinkedHashMap<>();
        try {
            DSLContext context = DSL.using(DB.getConnection(), SQLDialect.FIREBIRD_2_5);
            Result result = context.select(stringName,intKey)
                    .from(stringName.getTable())
                    .fetch();
            List names = result.getValues(stringName);
            List intKeys = result.getValues(intKey);
            for (int i=0;i<names.size();i++) {
                linkedHashMap.put((String)names.get(i),(Long)intKeys.get(i));
            }
        } catch (SQLException ex) {
            Logger.getGlobal().log(Level.SEVERE,"Ошибка при попытке прочитать словарь",ex);
        }
        return linkedHashMap;
    }
}