package lgk.nsbc.backend.info.patient;

import lgk.nsbc.backend.info.InfoType;
import lgk.nsbc.backend.info.searchable.Patient;
import org.jooq.Table;
import org.jooq.TableField;
import org.springframework.stereotype.Component;

import static lgk.nsbc.backend.info.InfoType.LIST;
import static lgk.nsbc.generated.tables.NbcPatientsDiagnosis.NBC_PATIENTS_DIAGNOSIS;

@Component
public class LgkDiagnosis implements Patient.PatientInfo{
    @Override
    public InfoType getInfoType() {
        return LIST;
    }

    @Override
    public String getRusName() {
        return "Диагноз по LGK";
    }

    @Override
    public TableField getTableField() {
        return NBC_PATIENTS_DIAGNOSIS.TEXT;
    }

    @Override
    public String toString() {
        return getRusName();
    }

    @Override
    public Table getTable() {
        return NBC_PATIENTS_DIAGNOSIS;
    }
}
