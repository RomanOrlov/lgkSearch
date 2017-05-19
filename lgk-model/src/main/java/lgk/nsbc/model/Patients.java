package lgk.nsbc.model;

import lombok.*;
import org.jooq.Record;

import java.io.Serializable;
import java.util.Date;

import static lgk.nsbc.generated.tables.NbcPatients.NBC_PATIENTS;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patients implements Serializable {
    private static final long serialVersionUID = 1L;

    private People people;
    private Long n;
    private Integer case_history_num;
    private Date case_history_date;
    private Long diagnosis;
    private Long nbc_organizations_n;

    /**
     * Нужен для SuggestionCombobox
     *
     * @return
     */
    public String getRepresentationName() {
        return people.getSurname() + " " + people.getName() + " " + people.getPatronymic();
    }

    public static Patients buildFromRecord(Record record) {
        return builder()
                .n(record.get(NBC_PATIENTS.N))
                .case_history_num(record.get(NBC_PATIENTS.CASE_HISTORY_NUM))
                .case_history_date(record.get(NBC_PATIENTS.CASE_HISTORY_DATE))
                .diagnosis(record.get(NBC_PATIENTS.DIAGNOSIS))
                .nbc_organizations_n(record.get(NBC_PATIENTS.NBC_ORGANIZATIONS_N))
                .build();
    }

    @Override
    public String toString() {
        return people.getSurname() + " " + people.getName() + " " + people.getPatronymic();
    }

    public String toStringWithCaseHistory() {
        StringBuilder builder = new StringBuilder();
        if (case_history_num != null) {
            builder.append(case_history_num);
        }
        if (case_history_date != null) {
            String substring = Integer.toString(case_history_date.getYear()).substring(2);
            builder.append("/").append(substring);
        }
        builder.append(" ")
                .append(toString());
        return builder.toString();
    }
}
