package lgk.nsbc.model;

import lgk.nsbc.model.dictionary.*;
import lombok.*;
import org.jooq.Record;

import java.io.Serializable;
import java.util.Date;

import static lgk.nsbc.generated.tables.Proc.PROC;
import static lgk.nsbc.model.dao.dictionary.OrganizationsDao.getOrganizationsMap;
import static lgk.nsbc.model.dao.dictionary.ProcRtDeviceDao.getProcRtDeviceMap;
import static lgk.nsbc.model.dao.dictionary.ProcRtTechDao.getProcRtTechMap;
import static lgk.nsbc.model.dao.dictionary.ProcTimeApproxDao.getProcTimeApproxMap;
import static lgk.nsbc.model.dao.dictionary.ProcTypeDao.getProcTypeMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "n")
public class Proc implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private Long patientN;
    private ProcType procType;
    private Date procBeginTime;
    private Date procEndTime;
    private ProcTimeApprox procTimeApprox;
    private String comment;
    private Long studN;
    private String recommendation;
    private String studComment;
    private ProcRtDevice procRtDevice;
    private ProcRtTech procRtTech;
    private Long parentProc;
    private Organization organization;

    public static Proc buildFromRecord(Record record) {
        return builder()
                .n(record.get(PROC.N))
                .patientN(record.get(PROC.PATIENTS_N))
                .procType(getProcTypeMap().get(record.get(PROC.PROC_TYPE)))
                .procBeginTime(record.get(PROC.PROCBEGINTIME))
                .procEndTime(record.get(PROC.PROCENDTIME))
                .procTimeApprox(getProcTimeApproxMap().get(record.get(PROC.TIME_APPROX)))
                .comment(record.get(PROC.COMMENT))
                .studN(record.get(PROC.STUD_N))
                .recommendation(record.get(PROC.RECOMMENDATION))
                .studComment(record.get(PROC.STUD_COMMENT))
                .procRtDevice(getProcRtDeviceMap().get(record.get(PROC.RT_DEVICE)))
                .procRtTech(getProcRtTechMap().get(record.get(PROC.RT_TECH)))
                .parentProc(record.get(PROC.PARENT_PROC))
                .organization(getOrganizationsMap().get(record.get(PROC.ORGANIZATIONS_N)))
                .build();
    }

    @Override
    public String toString() {
        return n + " " + patientN + " " + procType + " " + procBeginTime + " " + procEndTime;
    }
}
