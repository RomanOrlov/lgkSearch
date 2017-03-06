package lgk.nsbc.template.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NbcPatient {
    public static final String relationName = "BAS_PEOPLE";

    public enum Props {
        n, op_create, nbc_organizations_n, nbc_staff_n, case_history_num, case_history_date, bas_people_n, represent, represent_telephone, diagnosis, nbc_diagnosis_n, full_diagnosis, stationary, allergy, information_source, folder, disorder_history, nbc_diag_2015_n, nbc_diag_loc_n
    }

    private BasPeople basPeople = new BasPeople();
    private Long n;
    private Integer case_history_num;
    private int diagnosis;
    private int nbc_organizations_n;


    public NbcPatient(BasPeople basPeople, Integer case_history_num, int nbc_organizations_n) {
        this.basPeople = basPeople;
        this.case_history_num = case_history_num;
        this.nbc_organizations_n = nbc_organizations_n;
    }
}
