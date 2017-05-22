package lgk.nsbc.spect.model;

import lgk.nsbc.generated.tables.records.NbcFlupSpectDataRecord;
import lgk.nsbc.generated.tables.records.NbcFollowupRecord;
import lgk.nsbc.generated.tables.records.NbcStudInjRecord;
import lgk.nsbc.generated.tables.records.NbcStudRecord;
import lgk.nsbc.model.*;
import lgk.nsbc.model.Target;
import lgk.nsbc.model.dao.*;
import lgk.nsbc.model.spect.ContourType;
import lgk.nsbc.model.spect.TargetType;
import lgk.nsbc.util.DateUtils;
import lgk.nsbc.spect.view.spectcrud.SpectGridDBData;
import lgk.nsbc.spect.view.spectcrud.SpectGridData;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static lgk.nsbc.generated.Tables.NBC_STUD_INJ;
import static lgk.nsbc.generated.tables.BasPeople.BAS_PEOPLE;
import static lgk.nsbc.generated.tables.NbcFlupSpectData.NBC_FLUP_SPECT_DATA;
import static lgk.nsbc.generated.tables.NbcFollowup.NBC_FOLLOWUP;
import static lgk.nsbc.generated.tables.NbcPatients.NBC_PATIENTS;
import static lgk.nsbc.generated.tables.NbcStud.NBC_STUD;
import static lgk.nsbc.generated.tables.NbcTarget.NBC_TARGET;
import static lgk.nsbc.generated.tables.NbcTargetTargettype.NBC_TARGET_TARGETTYPE;

@Service
public class SpectDataManager {
    @Autowired
    private DSLContext context;
    @Autowired
    private TargetDao targetDao;
    @Autowired
    private FollowUpDao followUpDao;
    @Autowired
    private FlupSpectDataDao flupSpectDataDao;
    @Autowired
    private StudDao studDao;
    @Autowired
    private StudInjDao studInjDao;

    /**
     * Этот метод должен быть чертовски быстрым.
     * Поэтому он
     *
     * @return
     */
    public List<SpectGridData> findAllData() {
        try {
            // Ищем все исследования
            Result<NbcStudRecord> studRecords = context.fetch(NBC_STUD, NBC_STUD.STUDY_TYPE.eq(11L));
            List<Long> studId = studRecords.stream()
                    .map(NbcStudRecord::getN)
                    .collect(toList());
            CompletableFuture<Result<NbcFollowupRecord>> followUps = context.fetchAsync(NBC_FOLLOWUP, NBC_FOLLOWUP.NBC_STUD_N.in(studId))
                    .toCompletableFuture();
            List<Long> patientsId = studRecords.stream()
                    .map(NbcStudRecord::getNbcPatientsN)
                    .collect(toList());
            CompletableFuture<Result<Record>> patientsRecords = context.select()
                    .from(NBC_PATIENTS)
                    .leftJoin(BAS_PEOPLE).on(NBC_PATIENTS.BAS_PEOPLE_N.eq(BAS_PEOPLE.N))
                    .where(NBC_PATIENTS.N.in(patientsId))
                    .fetchAsync()
                    .toCompletableFuture();
            Map<Long, Stud> study = studRecords.stream().map(Stud::buildFromRecord)
                    .map(Optional::get)
                    .collect(toMap(Stud::getN, identity()));
            Map<Long, FollowUp> followup = followUps.get()
                    .stream()
                    .map(FollowUp::buildFromRecord)
                    .collect(toMap(FollowUp::getN, identity()));
            List<Long> targetsId = followup.values().stream()
                    .map(FollowUp::getNbc_target_n)
                    .collect(toList());
            CompletableFuture<Result<NbcFlupSpectDataRecord>> spectDataResult = context.fetchAsync(NBC_FLUP_SPECT_DATA, NBC_FLUP_SPECT_DATA.NBC_FOLLOWUP_N.in(followup.keySet()))
                    .toCompletableFuture();
            CompletableFuture<Result<NbcStudInjRecord>> inj = context.fetchAsync(NBC_STUD_INJ, NBC_STUD_INJ.NBC_STUD_N.in(studId))
                    .toCompletableFuture();
            Map<Long, StudInj> injByStudyId = inj.get()
                    .stream()
                    .map(StudInj::buildFromRecord)
                    .map(Optional::get)
                    .collect(toMap(StudInj::getNbc_stud_n, identity()));
            Map<Long, Patients> patients = patientsRecords.get()
                    .stream()
                    .map(record -> {
                        Patients nbcPatients = Patients.buildFromRecord(record);
                        People people = People.buildFromRecord(record);
                        nbcPatients.setPeople(people);
                        return nbcPatients;
                    }).collect(toMap(Patients::getN, identity()));
            CompletableFuture<Result<Record>> targetsResult = context.select()
                    .from(NBC_TARGET)
                    .leftJoin(NBC_TARGET_TARGETTYPE).on(NBC_TARGET.TARGETTYPE.eq(NBC_TARGET_TARGETTYPE.N))
                    .where(NBC_TARGET.NBC_PATIENTS_N.in(patients.keySet()))
                    .fetchAsync()
                    .toCompletableFuture();
            Map<Long, Target> targets = targetsResult.get().stream()
                    .map(Target::buildFromRecord)
                    .collect(toMap(Target::getN, identity()));
            Map<Long, List<FlupSpectData>> dataByFollowUp = spectDataResult.get()
                    .stream()
                    .map(FlupSpectData::buildFromRecord)
                    .collect(toMap(FlupSpectData::getNbc_followup_n, nbcFlupSpectData -> {
                        List<FlupSpectData> datas = new ArrayList<>();
                        datas.add(nbcFlupSpectData);
                        return datas;
                    }, (d1, d2) -> {
                        d1.addAll(d2);
                        return d1;
                    }));
            List<SpectGridData> gridData = followup.values()
                    .stream()
                    .map(nbcFollowUp -> {
                        Stud stud = study.get(nbcFollowUp.getNbc_stud_n());
                        if (stud == null) return Optional.empty();
                        Patients nbcPatients = patients.get(stud.getNbc_patients_n());
                        if (nbcPatients == null) return Optional.empty();
                        Target target = targets.get(nbcFollowUp.getNbc_target_n());
                        List<FlupSpectData> datas = dataByFollowUp.get(nbcFollowUp.getN());
                        if (datas == null) return Optional.empty();
                        StudInj studInj = injByStudyId.getOrDefault(stud.getN(), StudInj.builder().n(-1L)
                                .nbc_stud_n(stud.getN())
                                .build());
                        List<Target> allPatientsTargets = targets.values()
                                .stream()
                                .filter(t -> t.getNbcPatientsN().equals(nbcPatients.getN()))
                                .collect(Collectors.toList());
                        return Optional.of(new SpectGridDBData(nbcPatients, stud, nbcFollowUp, studInj, target, datas, allPatientsTargets).getSpectGridData());
                    })
                    .filter(Optional::isPresent)
                    .map(o -> (SpectGridData) (o.get()))
                    .collect(toList());
            return gridData;
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }

    public SpectGridData getBlankSpectGridData(Patients patients) {
        return new SpectGridDBData(patients, targetDao.getPatientsTargets(patients)).getSpectGridData();
    }

    public void persistSpectData(SpectGridData spectGridData) {
        SpectGridDBData spectGridDBData = spectGridData.getSpectGridDBData();
        // study
        Stud stud = spectGridDBData.getStud();
        if (stud.getN() == -1) {
            stud.setStudydatetime(DateUtils.asDate(spectGridData.getStudyDate()));
            studDao.createNbcStud(stud);
        } else if (!spectGridData.getStudyDate().equals(DateUtils.asLocalDate(stud.getStudydatetime()))) {
            studDao.updateStudy(stud);
        }
        // inj
        StudInj studInj = spectGridDBData.getStudInj();
        if (studInj.getN() == -1) {
            studInj.setInj_activity_bq(spectGridData.getDose());
            studInj.setNbc_stud_n(stud.getN());
            studInjDao.insertStudInj(studInj);
        } else if (!studInj.getInj_activity_bq().equals(spectGridData.getDose()) || !studInj.getNbc_stud_n().equals(stud.getN())) {
            studInj.setInj_activity_bq(spectGridData.getDose());
            studInj.setNbc_stud_n(stud.getN());
            studInjDao.updateInj(studInj);
        }
        FollowUp followUp = FollowUp.builder()
                .nbc_stud_n(stud.getN())
                .nbc_target_n(spectGridData.getTarget().getN())
                .build();
        List<FlupSpectData> data = createData(spectGridData);
        flupSpectDataDao.createSpectFollowUpData(followUp, data);

    }

    public void deleteSpectData(SpectGridData spectGridData) {
        SpectGridDBData spectGridDBData = spectGridData.getSpectGridDBData();
        // Нет данных.
        if (spectGridDBData.getFollowUp().getN() == -1) return;
        context.transaction(configuration -> {
            followUpDao.deleteFollowUp(spectGridDBData.getFollowUp());
            flupSpectDataDao.deleteSpectData(spectGridDBData.getFollowUp());
        });
    }

    private List<FlupSpectData> createData(SpectGridData spectGridData) {
        FlupSpectData hyp = FlupSpectData.builder()
                .targetType(TargetType.HYP)
                .contourType(ContourType.SPHERE)
                .volume(spectGridData.getHypVolume())
                .early_phase(spectGridData.getHypMin30())
                .late_phase(spectGridData.getHypMin60())
                .build();
        FlupSpectData hizSphere = FlupSpectData.builder()
                .targetType(TargetType.HIZ)
                .contourType(ContourType.SPHERE)
                .volume(spectGridData.getHizSphereVolume())
                .early_phase(spectGridData.getHizSphereMin30())
                .late_phase(spectGridData.getHizSphereMin60())
                .build();

        FlupSpectData hizIsoline10 = FlupSpectData.builder()
                .targetType(TargetType.HIZ)
                .contourType(ContourType.ISOLYNE10)
                .volume(spectGridData.getHizIsoline10Volume())
                .early_phase(spectGridData.getHizIsoline10Min30())
                .late_phase(spectGridData.getHizIsoline10Min60())
                .build();

        FlupSpectData hizIsoline25 = FlupSpectData.builder()
                .targetType(TargetType.HIZ)
                .contourType(ContourType.ISOLYNE25)
                .volume(spectGridData.getHizIsoline25Volume())
                .early_phase(spectGridData.getHizIsoline25Min30())
                .late_phase(spectGridData.getHizIsoline25Min60())
                .build();

        FlupSpectData targetSphere = FlupSpectData.builder()
                .targetType(TargetType.TARGET)
                .contourType(ContourType.SPHERE)
                .volume(spectGridData.getTargetSphereVolume())
                .early_phase(spectGridData.getTargetSphereMin30())
                .late_phase(spectGridData.getTargetSphereMin60())
                .build();

        FlupSpectData targetIsoline10 = FlupSpectData.builder()
                .targetType(TargetType.TARGET)
                .contourType(ContourType.ISOLYNE10)
                .volume(spectGridData.getTargetIsoline10Volume())
                .early_phase(spectGridData.getTargetIsoline10Min30())
                .late_phase(spectGridData.getTargetIsoline10Min60())
                .build();

        FlupSpectData targetIsoline25 = FlupSpectData.builder()
                .targetType(TargetType.TARGET)
                .contourType(ContourType.ISOLYNE25)
                .volume(spectGridData.getTargetIsoline25Volume())
                .early_phase(spectGridData.getTargetIsoline25Min30())
                .late_phase(spectGridData.getTargetIsoline25Min60())
                .build();
        return Arrays.asList(hyp, hizSphere, hizIsoline10, hizIsoline25, targetSphere, targetIsoline10, targetIsoline25);
    }
}
