package lgk.nsbc.spect.view.statistic;

import lgk.nsbc.model.Patients;
import lgk.nsbc.model.Proc;
import lgk.nsbc.model.SamplePatients;
import lgk.nsbc.model.dao.PatientsDao;
import lgk.nsbc.model.dao.ProcDao;
import lgk.nsbc.model.dao.SamplePatientsDao;
import lgk.nsbc.model.dao.dictionary.GenesDao;
import lgk.nsbc.model.dao.histology.HistologyDao;
import lgk.nsbc.model.dao.histology.MutationsDao;
import lgk.nsbc.model.dictionary.DicYesNo;
import lgk.nsbc.model.dictionary.Gene;
import lgk.nsbc.model.histology.Histology;
import lgk.nsbc.model.histology.Mutation;
import lgk.nsbc.spect.model.SpectDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class SampleManager implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final Long spectSampleId = 41L;
    public static final Long surgeryProcedureId = 10L;
    public static final Long radioTherapyProcId = 7L;
    @Autowired
    private SamplePatientsDao samplePatientsDao;
    @Autowired
    private PatientsDao patientsDao;
    @Autowired
    private ProcDao procDao;
    @Autowired
    private SpectDataManager spectDataManager;
    @Autowired
    private HistologyDao histologyDao;
    @Autowired
    private MutationsDao mutationsDao;

    public List<SampleBind> getSpectSample() {
        List<SamplePatients> samplePatients = samplePatientsDao.findSamplePatients(spectSampleId);
        List<Long> patientsId = samplePatients.stream()
                .map(SamplePatients::getPatient)
                .collect(toList());
        Map<Long, Patients> patientsMap = patientsDao.findPatientsWithIdIn(patientsId)
                .stream()
                .collect(toMap(Patients::getN, identity()));
        Map<Long, Proc> patientEarlySurgery = getPatientEarlySurgery(samplePatients, patientsId);
        Map<Long, Proc> radiotherapyProc = getPatientsRadiotherapyProc(samplePatients, patientEarlySurgery, patientsId);

        Map<Long, Map<Gene, DicYesNo>> histologyMap = getPatientMutations(samplePatients, patientsId);
        System.out.println(histologyMap);
        return samplePatients
                .stream()
                .filter(samplePatient -> patientsMap.get(samplePatient.getPatient()) != null)
                .map(sample -> {
                    Long patientId = sample.getPatient();
                    Map<Gene, DicYesNo> genes = histologyMap.get(patientId);
                    if (genes == null)
                        genes = Collections.emptyMap();
                    Map<Long, Gene> geneMap = GenesDao.getGenes();
                    return SampleBind.builder()
                            .patients(patientsMap.get(patientId))
                            .samplePatients(sample)
                            .surgeryProc(patientEarlySurgery.get(patientId))
                            .radioProc(radiotherapyProc.get(patientId))
                            .idh1(genes.get(geneMap.get(6L)))
                            .idh2(genes.get(geneMap.get(7L)))
                            .mgmt(genes.get(geneMap.get(8L)))
                            .build();
                })
                .collect(toList());
    }

    private Map<Long, Map<Gene, DicYesNo>> getPatientMutations(List<SamplePatients> samplePatients, List<Long> patientsId) {
        List<Histology> patientsHistology = histologyDao.findByPatientsId(patientsId);
        Map<Long, List<Mutation>> mutations = mutationsDao.findMutationsByHistologyList(patientsHistology);
        Map<Long, Map<Gene, DicYesNo>> genesByHistology = mutations.entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, e -> e.getValue()
                        .stream()
                        .collect(toMap(Mutation::getGene, Mutation::getDicYesNo)))
                );
        return samplePatients.stream()
                .map(patient -> getPatientHistology(patientsHistology, patient))
                .filter(histologyList -> !histologyList.isEmpty())
                .map(histologyList -> histologyList.get(histologyList.size() - 1))
                .filter(histology -> genesByHistology.containsKey(histology.getN()))
                .collect(toMap(Histology::getNbcPatientsN, histology -> genesByHistology.get(histology.getN())));
    }

    private List<Histology> getPatientHistology(List<Histology> patientsHistology, SamplePatients patient) {
        return patientsHistology.stream()
                .filter(histology -> Objects.equals(histology.getNbcPatientsN(), patient.getPatient()))
                .sorted(Comparator.comparing(Histology::getN))
                .collect(toList());
    }

    private Map<Long, Proc> getPatientsRadiotherapyProc(List<SamplePatients> samplePatients, Map<Long, Proc> patientEarlySurgery, List<Long> patientsId) {
        List<Proc> patientsRadiotherapy = procDao.findPatientsProcedures(patientsId, radioTherapyProcId);
        return samplePatients.stream()
                .map(patient -> findPatientsProc(patientsRadiotherapy, patient.getPatient()))
                .filter(procs -> !procs.isEmpty())
                .map(procs -> findRadiotherapy(procs, patientEarlySurgery.get(procs.get(0).getPatientN())))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toMap(Proc::getPatientN, identity()));
    }

    private Map<Long, Proc> getPatientEarlySurgery(List<SamplePatients> samplePatients, List<Long> patientsId) {
        List<Proc> patientsSurgery = procDao.findPatientsProcedures(patientsId, surgeryProcedureId);
        return samplePatients.stream()
                .map(patient -> findPatientsProc(patientsSurgery, patient.getPatient()))
                .map(this::findEarlyProcedure)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(Proc::getPatientN, identity()));
    }

    private List<Proc> findPatientsProc(List<Proc> procedures, Long patientId) {
        return procedures.stream()
                .filter(proc -> Objects.equals(proc.getPatientN(), patientId))
                .collect(toList());
    }

    private Optional<Proc> findEarlyProcedure(List<Proc> procedures) {
        return procedures.stream()
                .filter(proc -> proc.getProcBeginTime() != null)
                .sorted(Comparator.comparing(Proc::getProcBeginTime))
                .findFirst();
    }

    private Optional<Proc> findRadiotherapy(List<Proc> procedures, Proc earlySurgery) {
        if (earlySurgery == null)
            return findEarlyProcedure(procedures); // Если нет информации об операции пытаемся найти любую раннюю рт
        return procedures.stream()
                .filter(proc -> proc.getProcBeginTime() != null)
                .filter(proc -> proc.getProcBeginTime().after(earlySurgery.getProcBeginTime())) // РТ позже хирургии!
                .sorted(Comparator.comparing(Proc::getProcBeginTime))
                .findFirst(); // Самую ранную из всех, что после РТ
    }
}
