package lgk.nsbc.spect.model;

import lgk.nsbc.generated.tables.records.FlupSpectDataRecord;
import lgk.nsbc.generated.tables.records.FollowupRecord;
import lgk.nsbc.generated.tables.records.StudInjRecord;
import lgk.nsbc.generated.tables.records.StudRecord;
import lgk.nsbc.model.*;
import lgk.nsbc.model.Target;
import lgk.nsbc.model.dao.*;
import lgk.nsbc.model.spect.ContourType;
import lgk.nsbc.model.spect.TargetType;
import lgk.nsbc.spect.view.spectcrud.SpectGridDBData;
import lgk.nsbc.spect.view.spectcrud.SpectGridData;
import lgk.nsbc.util.DateUtils;
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
import static lgk.nsbc.generated.Tables.STUD_INJ;
import static lgk.nsbc.generated.tables.People.PEOPLE;
import static lgk.nsbc.generated.tables.FlupSpectData.FLUP_SPECT_DATA;
import static lgk.nsbc.generated.tables.Followup.FOLLOWUP;
import static lgk.nsbc.generated.tables.Patients.PATIENTS;
import static lgk.nsbc.generated.tables.Stud.STUD;
import static lgk.nsbc.generated.tables.Target.TARGET;
import static lgk.nsbc.generated.tables.TargetTargettype.TARGET_TARGETTYPE;

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
     * Поэтому он такой ебанутый
     *
     * @return
     */
    public List<SpectGridData> findAllData() {
        try {
            // Ищем все исследования
            Result<StudRecord> studRecords = context.fetch(STUD, STUD.STUDY_TYPE.eq(11L));
            List<Long> studId = studRecords.stream()
                    .map(StudRecord::getN)
                    .collect(toList());
            CompletableFuture<Result<FollowupRecord>> followUps = context.fetchAsync(FOLLOWUP, FOLLOWUP.STUD_N.in(studId))
                    .toCompletableFuture();
            List<Long> patientsId = studRecords.stream()
                    .map(StudRecord::getNbcPatientsN)
                    .collect(toList());
            CompletableFuture<Result<Record>> patientsRecords = context.select()
                    .from(PATIENTS)
                    .leftJoin(PEOPLE).on(PATIENTS.PEOPLE_N.eq(PEOPLE.N))
                    .where(PATIENTS.N.in(patientsId))
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
                    .map(FollowUp::getTargetN)
                    .collect(toList());
            CompletableFuture<Result<FlupSpectDataRecord>> spectDataResult = context.fetchAsync(FLUP_SPECT_DATA, FLUP_SPECT_DATA.FOLLOWUP_N.in(followup.keySet()))
                    .toCompletableFuture();
            CompletableFuture<Result<StudInjRecord>> inj = context.fetchAsync(STUD_INJ, STUD_INJ.STUD_N.in(studId))
                    .toCompletableFuture();
            Map<Long, StudInj> injByStudyId = inj.get()
                    .stream()
                    .map(StudInj::buildFromRecord)
                    .map(Optional::get)
                    .collect(toMap(StudInj::getStudN, identity()));
            Map<Long, Patients> patients = patientsRecords.get()
                    .stream()
                    .map(record -> {
                        Patients nbcPatients = Patients.buildFromRecord(record);
                        People people = People.buildFromRecord(record);
                        nbcPatients.setPeople(people);
                        return nbcPatients;
                    }).collect(toMap(Patients::getN, identity()));
            CompletableFuture<Result<Record>> targetsResult = context.select()
                    .from(TARGET)
                    .leftJoin(TARGET_TARGETTYPE).on(TARGET.TARGETTYPE.eq(TARGET_TARGETTYPE.N))
                    .where(TARGET.PATIENTS_N.in(patients.keySet()))
                    .fetchAsync()
                    .toCompletableFuture();
            Map<Long, Target> targets = targetsResult.get().stream()
                    .map(Target::buildFromRecord)
                    .collect(toMap(Target::getN, identity()));
            Map<Long, List<FlupSpectData>> dataByFollowUp = spectDataResult.get()
                    .stream()
                    .map(FlupSpectData::buildFromRecord)
                    .collect(toMap(FlupSpectData::getFollowupN, nbcFlupSpectData -> {
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
                        Stud stud = study.get(nbcFollowUp.getStudN());
                        if (stud == null) return Optional.empty();
                        Patients nbcPatients = patients.get(stud.getPatientsN());
                        if (nbcPatients == null) return Optional.empty();
                        Target target = targets.get(nbcFollowUp.getTargetN());
                        List<FlupSpectData> datas = dataByFollowUp.get(nbcFollowUp.getN());
                        if (datas == null) return Optional.empty();
                        StudInj studInj = injByStudyId.getOrDefault(stud.getN(), StudInj.builder().n(-1L)
                                .studN(stud.getN())
                                .build());
                        List<Target> allPatientsTargets = targets.values()
                                .stream()
                                .filter(t -> t.getPatientsN().equals(nbcPatients.getN()))
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
            stud.setStudyDateTime(DateUtils.asDate(spectGridData.getStudyDate()));
            studDao.createStud(stud);
        } else if (!spectGridData.getStudyDate().equals(DateUtils.asLocalDate(stud.getStudyDateTime()))) {
            studDao.updateStudy(stud);
        }
        // inj
        StudInj studInj = spectGridDBData.getStudInj();
        if (studInj.getN() == -1) {
            studInj.setInjActivityBq(spectGridData.getDose());
            studInj.setStudN(stud.getN());
            studInjDao.insertStudInj(studInj);
        } else if (!studInj.getInjActivityBq().equals(spectGridData.getDose()) || !studInj.getStudN().equals(stud.getN())) {
            studInj.setInjActivityBq(spectGridData.getDose());
            studInj.setStudN(stud.getN());
            studInjDao.updateInj(studInj);
        }
        FollowUp followUp = FollowUp.builder()
                .studN(stud.getN())
                .targetN(spectGridData.getTarget() == null ? null : spectGridData.getTarget().getN())
                .build();
        List<FlupSpectData> data = createData(spectGridData);
        flupSpectDataDao.createSpectFollowUpData(followUp, data);

    }

    public void deleteSpectData(SpectGridData spectGridData) {
        SpectGridDBData spectGridDBData = spectGridData.getSpectGridDBData();
        // Нет данных.
        if (spectGridDBData.getFollowUp().getN() == -1) return;
        followUpDao.deleteFollowUp(spectGridDBData.getFollowUp());
        flupSpectDataDao.deleteSpectData(spectGridDBData.getFollowUp());
    }

    private List<FlupSpectData> createData(SpectGridData spectGridData) {
        FlupSpectData hyp = FlupSpectData.builder()
                .targetType(TargetType.HYP)
                .contourType(ContourType.SPHERE)
                .volume(spectGridData.getHypVolume())
                .earlyPhase(spectGridData.getHypMin30())
                .latePhase(spectGridData.getHypMin60())
                .build();
        FlupSpectData hizSphere = FlupSpectData.builder()
                .targetType(TargetType.HIZ)
                .contourType(ContourType.SPHERE)
                .volume(spectGridData.getHizSphereVolume())
                .earlyPhase(spectGridData.getHizSphereMin30())
                .latePhase(spectGridData.getHizSphereMin60())
                .build();

        FlupSpectData hizIsoline10 = FlupSpectData.builder()
                .targetType(TargetType.HIZ)
                .contourType(ContourType.ISOLYNE10)
                .volume(spectGridData.getHizIsoline10Volume())
                .earlyPhase(spectGridData.getHizIsoline10Min30())
                .latePhase(spectGridData.getHizIsoline10Min60())
                .build();

        FlupSpectData hizIsoline25 = FlupSpectData.builder()
                .targetType(TargetType.HIZ)
                .contourType(ContourType.ISOLYNE25)
                .volume(spectGridData.getHizIsoline25Volume())
                .earlyPhase(spectGridData.getHizIsoline25Min30())
                .latePhase(spectGridData.getHizIsoline25Min60())
                .build();

        FlupSpectData hizIsoline50 = FlupSpectData.builder()
                .targetType(TargetType.HIZ)
                .contourType(ContourType.ISOLYNE50)
                .volume(spectGridData.getHizIsoline50Volume())
                .earlyPhase(spectGridData.getHizIsoline50Min30())
                .latePhase(spectGridData.getHizIsoline50Min60())
                .build();

        FlupSpectData targetSphere = FlupSpectData.builder()
                .targetType(TargetType.TARGET)
                .contourType(ContourType.SPHERE)
                .volume(spectGridData.getTargetSphereVolume())
                .earlyPhase(spectGridData.getTargetSphereMin30())
                .latePhase(spectGridData.getTargetSphereMin60())
                .build();

        FlupSpectData targetIsoline10 = FlupSpectData.builder()
                .targetType(TargetType.TARGET)
                .contourType(ContourType.ISOLYNE10)
                .volume(spectGridData.getTargetIsoline10Volume())
                .earlyPhase(spectGridData.getTargetIsoline10Min30())
                .latePhase(spectGridData.getTargetIsoline10Min60())
                .build();

        FlupSpectData targetIsoline25 = FlupSpectData.builder()
                .targetType(TargetType.TARGET)
                .contourType(ContourType.ISOLYNE25)
                .volume(spectGridData.getTargetIsoline25Volume())
                .earlyPhase(spectGridData.getTargetIsoline25Min30())
                .latePhase(spectGridData.getTargetIsoline25Min60())
                .build();

        FlupSpectData targetIsoline50 = FlupSpectData.builder()
                .targetType(TargetType.TARGET)
                .contourType(ContourType.ISOLYNE50)
                .volume(spectGridData.getTargetIsoline50Volume())
                .earlyPhase(spectGridData.getTargetIsoline50Min30())
                .latePhase(spectGridData.getTargetIsoline50Min60())
                .build();
        return Arrays.asList(hyp, hizSphere, hizIsoline10, hizIsoline25, hizIsoline50, targetSphere, targetIsoline10, targetIsoline25, targetIsoline50);
    }
}
