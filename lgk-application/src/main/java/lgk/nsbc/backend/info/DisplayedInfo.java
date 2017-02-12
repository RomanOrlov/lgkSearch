package lgk.nsbc.backend.info;

import org.jooq.Table;
import org.jooq.TableField;

public class DisplayedInfo implements SelectableInfo,ViewableInfo {
    private boolean isSelected;
    private final ViewableInfo viewableInfo;

    public DisplayedInfo(ViewableInfo viewableInfo) {
        this.viewableInfo = viewableInfo;
    }

    @Override
    public String getRusName() {
        return viewableInfo.getRusName();
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public InfoType getInfoType() {
        return viewableInfo.getInfoType();
    }

    @Override
    public TableField getTableField() {
        return viewableInfo.getTableField();
    }

    @Override
    public Table getTable() {
        return viewableInfo.getTable();
    }
}
