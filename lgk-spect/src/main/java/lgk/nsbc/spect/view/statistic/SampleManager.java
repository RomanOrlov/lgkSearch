package lgk.nsbc.spect.view.statistic;

import lgk.nsbc.model.Patients;
import lgk.nsbc.model.Proc;
import lgk.nsbc.model.SamplePatients;
import lgk.nsbc.model.dao.PatientsDao;
import lgk.nsbc.model.dao.ProcDao;
import lgk.nsbc.model.dao.SamplePatientsDao;
import lgk.nsbc.model.dao.histology.HistologyDao;
import lgk.nsbc.model.dao.histology.MutationsDao;
import lgk.nsbc.model.dictionary.DicYesNo;
import lgk.nsbc.model.dictionary.Gene;
import lgk.nsbc.model.histology.Histology;
import lgk.nsbc.model.histology.Mutation;
import lgk.nsbc.spect.model.SpectDataManager;
import lgk.nsbc.spect.view.spectcrud.SpectGridData;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static lgk.nsbc.model.dao.dictionary.GenesDao.getGenes;

@Service
public class SampleManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Long spectSampleId = 41L;
    private static final Long surgeryProcedureId = 10L;
    private static final Long radioTherapyProcId = 7L;

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
    @Autowired
    private DSLContext context;

    public List<SampleBind> getSpectSample() {
        List<SamplePatients> samplePatients = samplePatientsDao.findSamplePatients(spectSampleId);
        List<Long> patientsId = samplePatients.stream()
                .map(SamplePatients::getPatientId)
                .collect(toList());
        Map<Long, Patients> patientsMap = patientsDao.findPatientsWithIdIn(patientsId)
                .stream()
                .collect(toMap(Patients::getN, identity()));
        Map<Long, Proc> patientEarlySurgery = getPatientEarlySurgery(samplePatients, patientsId);
        Map<Long, Proc> radiotherapyProc = getPatientsRadiotherapyProc(samplePatients, patientEarlySurgery, patientsId);
        Map<Long, Map<Gene, DicYesNo>> histologyMap = getPatientMutations(samplePatients, patientsId);

        // Madness Пытаемся найти данные по id пациента, потом по истории болезни, потом в отчаянии по полному имени
        Map<Long, List<SpectGridData>> patientSpectDataByPatientId = getPatientsSpectData(samplePatients, patientsMap);
        Map<String, List<SpectGridData>> patientSpectDataByCaseHistoryNum = patientSpectDataByPatientId.entrySet()
                .stream()
                .collect(toMap(o -> o.getValue().get(0).getSpectGridDBData().getPatients().getCaseHistoryNumber(), Map.Entry::getValue));
        Map<String, List<SpectGridData>> patientSpectDataByFullName = patientSpectDataByPatientId.entrySet()
                .stream()
                .collect(toMap(o -> o.getValue().get(0).getSpectGridDBData().getPatients().getFullName(), Map.Entry::getValue));
        return samplePatients
                .stream()
                .filter(samplePatient -> patientsMap.get(samplePatient.getPatientId()) != null)
                .map(sample -> {
                    Long patientId = sample.getPatientId();
                    Patients patients = patientsMap.get(patientId);
                    Map<Gene, DicYesNo> genes = histologyMap.getOrDefault(patientId, emptyMap());
                    List<SpectGridData> spectGridData = patientSpectDataByPatientId.getOrDefault(patientId,
                            patientSpectDataByCaseHistoryNum.getOrDefault(patients.getCaseHistoryNumber(),
                                    patientSpectDataByFullName.getOrDefault(patients.getFullName(), emptyList())));
                    Map<Long, Gene> geneMap = getGenes();
                    return SampleBind.builder()
                            .patients(patients)
                            .samplePatients(sample)
                            .surgeryProc(patientEarlySurgery.get(patientId))
                            .radioProc(radiotherapyProc.get(patientId))
                            .idh1(genes.get(geneMap.get(6L)))
                            .idh2(genes.get(geneMap.get(7L)))
                            .mgmt(genes.get(geneMap.get(8L)))
                            .spect1(spectGridData.size() > 0 ? spectGridData.get(0) : null)
                            .spect2(spectGridData.size() > 1 ? spectGridData.get(1) : null)
                            .spect3(spectGridData.size() > 2 ? spectGridData.get(2) : null)
                            .build();
                })
                //.filter(sampleBind -> "Y".equals(sampleBind.getSamplePatients().getInclusion()))
                .collect(toList());
    }

    private Map<Long, List<SpectGridData>> getPatientsSpectData(List<SamplePatients> samplePatients, Map<Long, Patients> patientsMap) {
        List<SpectGridData> spectData = spectDataManager.findAllData();
        spectData.sort(Comparator.comparing(SpectGridData::getStudyDate));
        return samplePatients.stream()
                .map(patients -> findPatientSpect(spectData, patientsMap.get(patients.getPatientId())))
                .filter(spectGridData -> !spectGridData.isEmpty())
                .map(this::splitPatientSpectDataByData)
                .collect(toMap(o -> o.get(0).getSpectGridDBData().getPatients().getN(), identity()));
    }

    /**
     * Вернет по одной офект в день, в случае если запись велась для более чем одной мишени.
     *
     * @param spectGridData
     * @return
     */
    private List<SpectGridData> splitPatientSpectDataByData(List<SpectGridData> spectGridData) {
        List<LocalDate> spectDates = spectGridData.stream()
                .map(SpectGridData::getStudyDate)
                .distinct()
                .collect(toList());
        if (spectDates.size() == spectGridData.size())
            return spectGridData;
        List<SpectGridData> differentByDateSpect = spectDates.stream()
                .map(date -> findPatientSpectByDate(spectGridData, date))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
        return differentByDateSpect;
    }

    private Optional<SpectGridData> findPatientSpectByDate(List<SpectGridData> spectGridData, LocalDate localDate) {
        List<SpectGridData> patientSpectAtDateByInEarly = spectGridData.stream()
                .filter(spect -> localDate.isEqual(spect.getStudyDate()))
                .sorted(Comparator.comparing(SpectGridData::getInEarly))
                .collect(toList());
        if (patientSpectAtDateByInEarly.isEmpty())
            return Optional.empty();
        return Optional.of(patientSpectAtDateByInEarly.get(0));
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
                .collect(toMap(Histology::getPatientsN, histology -> genesByHistology.get(histology.getN())));
    }

    private List<Histology> getPatientHistology(List<Histology> patientsHistology, SamplePatients patient) {
        return patientsHistology.stream()
                .filter(histology -> Objects.equals(histology.getPatientsN(), patient.getPatientId()))
                .sorted(Comparator.comparing(Histology::getN))
                .collect(toList());
    }

    private Map<Long, Proc> getPatientsRadiotherapyProc(List<SamplePatients> samplePatients, Map<Long, Proc> patientEarlySurgery, List<Long> patientsId) {
        List<Proc> patientsRadiotherapy = procDao.findPatientsProcedures(patientsId, radioTherapyProcId);
        return samplePatients.stream()
                .map(patient -> findPatientsProc(patientsRadiotherapy, patient.getPatientId()))
                .filter(procs -> !procs.isEmpty())
                .map(procs -> findRadiotherapy(procs, patientEarlySurgery.get(procs.get(0).getPatientN())))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toMap(Proc::getPatientN, identity()));
    }

    private Map<Long, Proc> getPatientEarlySurgery(List<SamplePatients> samplePatients, List<Long> patientsId) {
        List<Proc> patientsSurgery = procDao.findPatientsProcedures(patientsId, surgeryProcedureId);
        return samplePatients.stream()
                .map(patient -> findPatientsProc(patientsSurgery, patient.getPatientId()))
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

    private List<SpectGridData> findPatientSpect(List<SpectGridData> spectDatа, Patients patients) {
        return spectDatа.stream()
                .filter(spectGridData -> {
                    Patients patient = spectGridData.getSpectGridDBData().getPatients();
                    return Objects.equals(patient.getN(), patients.getN())
                            || Objects.equals(patient.getFullName(), patients.getFullName())
                            || Objects.equals(patient.getCaseHistoryNumber(), patients.getCaseHistoryNumber());
                })
                .filter(spectGridData -> spectGridData.getStudyDate() != null)
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

    public void updateSamplePatient(SampleBind bean) {
        context.transaction(configuration -> {
            samplePatientsDao.updateSamplePatient(bean.getSamplePatients());
        });
    }

    public void removeSamplePatient(SampleBind value) {
        if (value.getSamplePatients() != null && value.getSamplePatients().getN() != null) {
            context.transaction(configuration -> {
                samplePatientsDao.removeSamplePatient(value.getSamplePatients());
            });
        }
    }

    public SampleBind getTamplateSimpleBind(Patients patients) {
        SamplePatients samplePatients = SamplePatients.builder()
                .patientId(patients.getN())
                .inclusion("N")
                .sampleId(spectSampleId)
                .build();
        samplePatientsDao.saveSamplePatients(samplePatients);
        SampleBind sampleBind = SampleBind.builder()
                .patients(patients)
                .samplePatients(samplePatients)
                .build();
        return sampleBind;
    }
}
