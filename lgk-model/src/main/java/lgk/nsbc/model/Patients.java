package lgk.nsbc.model;

import lombok.*;
import org.jooq.Record;

import static lgk.nsbc.generated.tables.NbcPatients.NBC_PATIENTS;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patients {
    private People people;
    private Long n;
    private Integer case_history_num;
    private Long diagnosis;
    private Long nbc_organizations_n;

    /**
     * Нужен для SuggestionCombobox
     * @return
     */
    public String getRepresentationName() {
        return people.getSurname() + " " + people.getName() + " " + people.getPatronymic();
    }

    public static Patients buildFromRecord(Record record) {
        return builder()
                .n(record.get(NBC_PATIENTS.N))
                .case_history_num(record.get(NBC_PATIENTS.CASE_HISTORY_NUM))
                .diagnosis(record.get(NBC_PATIENTS.DIAGNOSIS))
                .nbc_organizations_n(record.get(NBC_PATIENTS.NBC_ORGANIZATIONS_N))
                .build();
    }

    @Override
    public String toString() {
        return case_history_num + " " + people.getSurname() + " " + people.getName() + " " + people.getPatronymic();
    }
}
