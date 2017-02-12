package lgk.nsbc.backend.info.common;

import lgk.nsbc.backend.info.InfoType;
import lgk.nsbc.backend.info.searchable.Patient;
import org.jooq.Table;
import org.jooq.TableField;
import org.springframework.stereotype.Component;

import static lgk.nsbc.backend.info.InfoType.TEXT;
import static lgk.nsbc.generated.tables.BasPeople.BAS_PEOPLE;

@Component
public class BasPeoplePatronymic implements Patient.PatientInfo {
    @Override
    public InfoType getInfoType() {
        return TEXT;
    }

    @Override
    public String getRusName() {
        return "Отчество";
    }

    @Override
    public TableField getTableField() {
        return BAS_PEOPLE.PATRONYMIC;
    }


    @Override
    public String toString() {
        return getRusName();
    }

    @Override
    public Table getTable() {
        return BAS_PEOPLE;
    }
}
