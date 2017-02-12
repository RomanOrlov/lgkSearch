package lgk.nsbc.backend.info.searchable;

import lgk.nsbc.backend.info.DisplayedInfo;
import lgk.nsbc.backend.info.ViewableInfo;
import lgk.nsbc.backend.info.criteria.Criteria;
import lgk.nsbc.generated.tables.records.NbcPatientsRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static lgk.nsbc.generated.tables.BasPeople.BAS_PEOPLE;
import static lgk.nsbc.generated.tables.NbcDiag_2015.NBC_DIAG_2015;
import static lgk.nsbc.generated.tables.NbcOrganizations.NBC_ORGANIZATIONS;
import static lgk.nsbc.generated.tables.NbcPatients.NBC_PATIENTS;
import static lgk.nsbc.generated.tables.NbcPatientsDiagnosis.NBC_PATIENTS_DIAGNOSIS;
import static lgk.nsbc.generated.tables.NbcProc.NBC_PROC;
import static lgk.nsbc.generated.tables.NbcTarget.NBC_TARGET;

@Component
public class Patient extends SearchTarget<NbcPatientsRecord> {
    @Autowired
    private PatientInfo[] patientInfos;
    @Autowired
    private DSLContext dslContext;

    public Patient() {
        super("Пациент", NBC_PATIENTS, NBC_PATIENTS.N);

        // CЛОВАРИ
        // NBC_PATIENTS <-> NBC_PATIENTS_DIAGNOSIS (DICTIONARY)
        joinConditions.put(NBC_PATIENTS_DIAGNOSIS,
                NBC_PATIENTS.DIAGNOSIS.eq(NBC_PATIENTS_DIAGNOSIS.N));
        // NBC_PATIENTS <-> NBC_DIAG_2015 (DICTIONARY)
        joinConditions.put(NBC_DIAG_2015,
                NBC_PATIENTS.NBC_DIAG_2015_N.eq(NBC_DIAG_2015.N));
        // NBC_PATIENTS <-> NBC_ORGANIZATIONS (DICTIONARY)
        joinConditions.put(NBC_ORGANIZATIONS,
                NBC_PATIENTS.NBC_ORGANIZATIONS_N.eq(NBC_ORGANIZATIONS.N));

        // NBC_PATIENTS <-> BAS_PEOPLE
        joinConditions.put(BAS_PEOPLE,
                NBC_PATIENTS.BAS_PEOPLE_N.eq(BAS_PEOPLE.N));
        joinConditions.put(NBC_PROC,
                NBC_PATIENTS.N.eq(NBC_PROC.NBC_PATIENTS_N));
        // NBC_PATIENTS <-> NBC_TARGET
        joinConditions.put(NBC_TARGET,
                NBC_PATIENTS.N.eq(NBC_TARGET.NBC_PATIENTS_N));
    }

    @Override
    public List<Criteria> getCriteriaList() {
        return getCriteries(getDisplayedInfoList(), dslContext);
    }

    @Override
    public List<DisplayedInfo> getDisplayedInfoList() {
        return Arrays.stream(patientInfos).map(DisplayedInfo::new).collect(Collectors.toList());
    }

    public interface PatientInfo extends ViewableInfo {
    }
}
