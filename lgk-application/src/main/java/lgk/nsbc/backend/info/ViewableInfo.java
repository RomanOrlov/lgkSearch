package lgk.nsbc.backend.info;

import org.jooq.Table;
import org.jooq.TableField;

public interface ViewableInfo {
    String getRusName();

    InfoType getInfoType();

    TableField getTableField();

    Table getTable();
}
