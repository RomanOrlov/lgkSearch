package lgk.nsbc.backend.info.common;

import lgk.nsbc.backend.info.InfoType;
import lgk.nsbc.backend.info.searchable.Patient;
import org.jooq.Table;
import org.jooq.TableField;
import org.springframework.stereotype.Component;

import static lgk.nsbc.backend.info.InfoType.LIST;
import static lgk.nsbc.generated.tables.NbcOrganizations.NBC_ORGANIZATIONS;

@Component
public class NbcOrganizationsName implements Patient.PatientInfo {
    @Override
    public InfoType getInfoType() {
        return LIST;
    }

    @Override
    public String getRusName() {
        return "Организация";
    }

    @Override
    public TableField getTableField() {
        return NBC_ORGANIZATIONS.NAME;
    }

    @Override
    public String toString() {
        return getRusName();
    }

    @Override
    public Table getTable() {
        return NBC_ORGANIZATIONS;
    }
}
