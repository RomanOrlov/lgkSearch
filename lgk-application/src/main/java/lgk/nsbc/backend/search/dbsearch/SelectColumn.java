package lgk.nsbc.backend.search.dbsearch;

import lgk.nsbc.backend.search.SelectableInfo;
import org.jooq.Table;
import org.jooq.TableField;

public class SelectColumn implements SelectableInfo {
    private long n;
    private int uniqueNum;
    private boolean isSelected;
    private final String rusName;
    private final TableField tableField;
    private final CriteriaType criteriaType;

    public SelectColumn(int uniqueNum, CriteriaType type, String rusName, TableField tableField) {
        this.uniqueNum = uniqueNum;
        this.criteriaType = type;
        this.rusName = rusName;
        this.tableField = tableField;
    }

    public SelectColumn(SelectColumn selectColumn) {
        this.uniqueNum = selectColumn.getUniqueNum();
        this.criteriaType = selectColumn.getCriteriaType();
        this.rusName = selectColumn.getRusName();
        this.tableField = selectColumn.getTableField();
    }

    @Override
    public String getRusName() {
        return rusName;
    }

    @Override
    public int getUniqueNum() {
        return uniqueNum;
    }

    public void setUniqueNum(int uniqueNum) {
        this.uniqueNum = uniqueNum;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public TableField getTableField() {
        return tableField;
    }

    public Table getTable() {
        return tableField.getTable();
    }

    @Override
    public String toString() {
        return rusName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof SelectColumn)) {
            return false;
        }
        return ((SelectColumn) obj).getUniqueNum() == this.getUniqueNum();
    }

    public CriteriaType getCriteriaType() {
        return criteriaType;
    }

    @Override
    public long getN() {
        return n;
    }

    @Override
    public void setN(long n) {
        this.n = n;
    }
}