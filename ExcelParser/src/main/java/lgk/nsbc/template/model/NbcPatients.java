package lgk.nsbc.template.model;

import lgk.nsbc.generated.tables.records.NbcPatientsRecord;
import lgk.nsbc.view.RepresentationName;
import lombok.*;
import org.jooq.Record;

import static lgk.nsbc.generated.tables.NbcPatients.NBC_PATIENTS;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NbcPatients implements RepresentationName {

    private BasPeople basPeople = new BasPeople();
    private Long n;
    private Integer case_history_num;
    private Long diagnosis;
    private Long nbc_organizations_n;

    @Override
    public String toString() {
        return basPeople.toString() + " " + n + " " + case_history_num;
    }

    @Override
    public String getRepresentationName() {
        return toString();
    }

    public static NbcPatients buildFromRecord(Record record) {
        return builder()
                .n(record.get(NBC_PATIENTS.N))
                .case_history_num(record.get(NBC_PATIENTS.CASE_HISTORY_NUM))
                .diagnosis(record.get(NBC_PATIENTS.DIAGNOSIS))
                .nbc_organizations_n(record.get(NBC_PATIENTS.NBC_ORGANIZATIONS_N))
                .build();
    }
}
