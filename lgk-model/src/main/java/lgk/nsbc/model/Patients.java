package lgk.nsbc.model;

import lgk.nsbc.model.dictionary.Organization;
import lombok.*;
import org.jooq.Record;

import java.io.Serializable;
import java.util.Date;

import static lgk.nsbc.generated.tables.Patients.PATIENTS;
import static lgk.nsbc.model.dao.dictionary.OrganizationsDao.getOrganizationsMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patients implements Serializable {
    private static final long serialVersionUID = 1L;

    private People people;
    private Long n;
    private Integer caseHistoryNum;
    private Date caseHistoryDate;
    private Long diagnosis;
    private Organization organization;

    public static Patients buildFromRecord(Record record) {
        return builder()
                .n(record.get(PATIENTS.N))
                .caseHistoryNum(record.get(PATIENTS.CASE_HISTORY_NUM))
                .caseHistoryDate(record.get(PATIENTS.CASE_HISTORY_DATE))
                .diagnosis(record.get(PATIENTS.DIAGNOSIS))
                .organization(getOrganizationsMap().get(record.get(PATIENTS.ORGANIZATIONS_N)))
                .build();
    }

    @Override
    public String toString() {
        return getFullName();
    }

    public String getFullName() {
        return people.getFullName();
    }

    public String getCaseHistoryNumber() {
        StringBuilder builder = new StringBuilder();
        if (organization != null && organization.getSign() != null) {
            builder.append(organization.getSign())
                    .append(" ");
        }
        if (caseHistoryNum != null) {
            builder.append(caseHistoryNum);
        }
        if (caseHistoryDate != null) {
            String substring = caseHistoryDate.toString().substring(2, 4);
            builder.append("/").append(substring);
        }
        return builder.toString();
    }

    public String toStringWithCaseHistory() {
        return getFullName() + " " + getCaseHistoryNumber();
    }
}
