package lgk.nsbc.spect.view.statistic;

import lgk.nsbc.model.Patients;
import lgk.nsbc.model.Proc;
import lgk.nsbc.model.SamplePatients;
import lgk.nsbc.model.dao.PatientsDao;
import lgk.nsbc.model.dao.ProcDao;
import lgk.nsbc.model.dao.SamplePatientsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class SampleManager {
    public static final Long spectSampleId = 41L;
    public static final Long surgeryProcedureId = 10L;
    @Autowired
    private SamplePatientsDao samplePatientsDao;
    @Autowired
    private PatientsDao patientsDao;
    @Autowired
    private ProcDao procDao;

    public List<SampleBind> getSpectSample() {
        List<SamplePatients> samplePatients = samplePatientsDao.findSamplePatients(spectSampleId);
        List<Long> patientsId = samplePatients.stream()
                .map(SamplePatients::getPatient)
                .collect(toList());
        Map<Long, Patients> patientsMap = patientsDao.findPatientsWithIdIn(patientsId)
                .stream()
                .collect(toMap(Patients::getN, identity()));
        List<Proc> patientsProcedures = procDao.findPatientsProcedures(patientsId, surgeryProcedureId);
        Map<Long, Proc> patientEarlySurgery = samplePatients.stream()
                .map(patient -> findPatientsProc(patientsProcedures, patient.getPatient()))
                .map(this::findEarlySurgery)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(Proc::getPatientN, identity()));
        return samplePatients
                .stream()
                .filter(samplePatient -> patientsMap.get(samplePatient.getPatient()) != null)
                .map(sample -> SampleBind.builder()
                        .patients(patientsMap.get(sample.getPatient()))
                        .samplePatients(sample)
                        .surgeryProc(patientEarlySurgery.get(sample.getPatient()))
                        .build())
                .collect(toList());
    }

    private List<Proc> findPatientsProc(List<Proc> procedures, Long patientId) {
        return procedures.stream()
                .filter(proc -> Objects.equals(proc.getPatientN(), patientId))
                .collect(toList());
    }

    private Optional<Proc> findEarlySurgery(List<Proc> procedures) {
        return procedures.stream()
                .filter(proc -> proc.getProcBeginTime() != null)
                .sorted(Comparator.comparing(Proc::getProcBeginTime))
                .findFirst();
    }
}
