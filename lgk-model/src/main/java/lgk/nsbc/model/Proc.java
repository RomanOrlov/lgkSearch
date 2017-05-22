package lgk.nsbc.model;

import lgk.nsbc.model.dictionary.*;
import lombok.*;
import org.jooq.Record;

import java.io.Serializable;
import java.util.Date;

import static lgk.nsbc.generated.tables.NbcProc.NBC_PROC;
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
                .n(record.get(NBC_PROC.N))
                .patientN(record.get(NBC_PROC.NBC_PATIENTS_N))
                .procType(getProcTypeMap().get(record.get(NBC_PROC.PROC_TYPE)))
                .procBeginTime(record.get(NBC_PROC.PROCBEGINTIME))
                .procEndTime(record.get(NBC_PROC.PROCENDTIME))
                .procTimeApprox(getProcTimeApproxMap().get(record.get(NBC_PROC.TIME_APPROX)))
                .comment(record.get(NBC_PROC.COMMENT))
                .studN(record.get(NBC_PROC.NBC_STUD_N))
                .recommendation(record.get(NBC_PROC.RECOMMENDATION))
                .studComment(record.get(NBC_PROC.STUD_COMMENT))
                .procRtDevice(getProcRtDeviceMap().get(record.get(NBC_PROC.RT_DEVICE)))
                .procRtTech(getProcRtTechMap().get(record.get(NBC_PROC.RT_TECH)))
                .parentProc(record.get(NBC_PROC.PARENT_PROC))
                .organization(getOrganizationsMap().get(record.get(NBC_PROC.NBC_ORGANIZATIONS_N)))
                .build();
    }

    @Override
    public String toString() {
        return n + " " + patientN + " " + procType + " " + procBeginTime + " " + procEndTime;
    }
}
