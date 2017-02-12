package lgk.nsbc.backend.info.patient;

import lgk.nsbc.backend.info.InfoType;
import lgk.nsbc.backend.info.searchable.Patient;
import org.jooq.Table;
import org.jooq.TableField;
import org.springframework.stereotype.Component;

import static lgk.nsbc.backend.info.InfoType.LIST;
import static lgk.nsbc.generated.tables.NbcDiag_2015.NBC_DIAG_2015;

@Component
public class PrimaryDiagnosis implements Patient.PatientInfo {
    @Override
    public InfoType getInfoType() {
        return LIST;
    }

    @Override
    public String getRusName() {
        return "Основной диагноз";
    }

    @Override
    public TableField getTableField() {
        return NBC_DIAG_2015.TEXT;
    }

    @Override
    public String toString() {
        return getRusName();
    }

    @Override
    public Table getTable() {
        return NBC_DIAG_2015;
    }
}