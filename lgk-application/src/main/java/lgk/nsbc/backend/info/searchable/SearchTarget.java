package lgk.nsbc.backend.info.searchable;

import lgk.nsbc.backend.info.DisplayedInfo;
import lgk.nsbc.backend.info.criteria.*;
import org.jooq.*;

import java.util.*;
import java.util.stream.Collectors;

public abstract class SearchTarget<R extends Record> {
    private final Table<R> table;
    private final TableField<R, Long> uniqueKey;
    protected final Map<Table, Condition> joinConditions = new IdentityHashMap<>();
    private final String rusName;

    public SearchTarget(String rusName, Table<R> table, TableField<R, Long> uniqueKey) {
        this.rusName = rusName;
        this.table = table;
        this.uniqueKey = uniqueKey;
    }

    public String getRusName() {
        return rusName;
    }

    public Table<R> getTable() {
        return table;
    }

    public TableField<R, Long> getUniqueKey() {
        return uniqueKey;
    }

    public Condition getJoinCondition(Table table) {
        if (!joinConditions.containsKey(table))
            throw new RuntimeException("There is no table " + table.getName() + " join condition in " + rusName);
        return joinConditions.get(table);
    }

    public abstract List<Criteria> getCriteriaList();

    public abstract List<DisplayedInfo> getDisplayedInfoList();

    protected List<Criteria> getCriteries(List<DisplayedInfo> displayedInfos, DSLContext dslContext) {
        return displayedInfos.stream().map(displayedInfo -> {
            switch (displayedInfo.getInfoType()) {
                case DATE:
                    return new CriteriaDate(displayedInfo);
                case TEXT:
                    return new CriteriaText(displayedInfo);
                case NUMBER:
                    return new CriteriaNumber(displayedInfo);
                case LIST:
                    TableField uniqueKey = displayedInfo.getTableField().getTable().getPrimaryKey().getFieldsArray()[0];
                    Map<String, Long> map = getDictionaryValues(displayedInfo.getTableField(), uniqueKey, dslContext);
                    return new CriteriaList(displayedInfo, map, uniqueKey);
                default:
                    throw new RuntimeException("Unknown criteria type");
            }
        }).collect(Collectors.toList());
    }

    /**
     * @param stringName поле из словаря (Таблицы в базе), откуда будет браться название
     * @param intKey     поле для join, по которому будет подсоединяться словарь.
     * @return словарь
     */
    protected LinkedHashMap<String, Long> getDictionaryValues(TableField stringName, TableField intKey, DSLContext context) {
        LinkedHashMap<String, Long> linkedHashMap = new LinkedHashMap<>();
        Result result = context.select(stringName, intKey)
                .from(stringName.getTable())
                .fetch();
        List names = result.getValues(stringName);
        List intKeys = result.getValues(intKey);
        for (int i = 0; i < names.size(); i++) {
            linkedHashMap.put((String) names.get(i), (Long) intKeys.get(i));
        }
        return linkedHashMap;
    }


    @Override
    public String toString() {
        return rusName;
    }
}
