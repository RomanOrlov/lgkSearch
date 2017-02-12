package lgk.nsbc.backend.info.patient;

import lgk.nsbc.backend.info.InfoType;
import lgk.nsbc.backend.info.searchable.Patient;
import org.jooq.Table;
import org.jooq.TableField;
import org.springframework.stereotype.Component;

import static lgk.nsbc.backend.info.InfoType.TEXT;
import static lgk.nsbc.generated.tables.NbcPatients.NBC_PATIENTS;

@Component
public class ClinicDiagnosis implements Patient.PatientInfo {
    @Override
    public InfoType getInfoType() {
        return TEXT;
    }

    @Override
    public String getRusName() {
        return "Клинический диагноз";
    }

    @Override
    public TableField getTableField() {
        return NBC_PATIENTS.FULL_DIAGNOSIS;
    }

    @Override
    public String toString() {
        return getRusName();
    }

    @Override
    public Table getTable() {
        return NBC_PATIENTS;
    }
}
