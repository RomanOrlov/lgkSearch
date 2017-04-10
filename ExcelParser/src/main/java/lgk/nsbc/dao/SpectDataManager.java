package lgk.nsbc.dao;

import lgk.nsbc.generated.tables.records.NbcFlupSpectDataRecord;
import lgk.nsbc.generated.tables.records.NbcFollowupRecord;
import lgk.nsbc.generated.tables.records.NbcStudInjRecord;
import lgk.nsbc.generated.tables.records.NbcStudRecord;
import lgk.nsbc.model.*;
import lgk.nsbc.model.spect.ContourType;
import lgk.nsbc.model.spect.TargetType;
import lgk.nsbc.util.DateUtils;
import lgk.nsbc.view.spectcrud.SpectGridDBData;
import lgk.nsbc.view.spectcrud.SpectGridData;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

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
    private NbcTargetDao nbcTargetDao;
    @Autowired
    private NbcFollowUpDao nbcFollowUpDao;
    @Autowired
    private NbcFlupSpectDataDao nbcFlupSpectDataDao;
    @Autowired
    private NbcStudDao nbcStudDao;
    @Autowired
    private NbcStudInjDao nbcStudInjDao;

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
            Map<Long, NbcStud> study = studRecords.stream().map(NbcStud::buildFromRecord)
                    .map(Optional::get)
                    .collect(toMap(NbcStud::getN, identity()));
            Map<Long, NbcFollowUp> followup = followUps.get()
                    .stream()
                    .map(NbcFollowUp::buildFromRecord)
                    .collect(toMap(NbcFollowUp::getN, identity()));
            List<Long> targetsId = followup.values().stream()
                    .map(NbcFollowUp::getNbc_target_n)
                    .collect(toList());
            CompletableFuture<Result<NbcFlupSpectDataRecord>> spectDataResult = context.fetchAsync(NBC_FLUP_SPECT_DATA, NBC_FLUP_SPECT_DATA.NBC_FOLLOWUP_N.in(followup.keySet()))
                    .toCompletableFuture();
            CompletableFuture<Result<Record>> targetsResult = context.select()
                    .from(NBC_TARGET)
                    .leftJoin(NBC_TARGET_TARGETTYPE).on(NBC_TARGET.TARGETTYPE.eq(NBC_TARGET_TARGETTYPE.N))
                    .where(NBC_TARGET.N.in(targetsId))
                    .fetchAsync()
                    .toCompletableFuture();
            CompletableFuture<Result<NbcStudInjRecord>> inj = context.fetchAsync(NBC_STUD_INJ, NBC_STUD_INJ.NBC_STUD_N.in(studId))
                    .toCompletableFuture();
            Map<Long, NbcStudInj> injByStudyId = inj.get()
                    .stream()
                    .map(NbcStudInj::buildFromRecord)
                    .map(Optional::get)
                    .collect(toMap(NbcStudInj::getNbc_stud_n, identity()));
            Map<Long, NbcPatients> patients = patientsRecords.get()
                    .stream()
                    .map(record -> {
                        NbcPatients nbcPatients = NbcPatients.buildFromRecord(record);
                        BasPeople basPeople = BasPeople.buildFromRecord(record);
                        nbcPatients.setBasPeople(basPeople);
                        return nbcPatients;
                    }).collect(toMap(NbcPatients::getN, identity()));
            Map<Long, NbcTarget> targets = targetsResult.get().stream()
                    .map(NbcTarget::buildFromRecord)
                    .collect(toMap(NbcTarget::getN, identity()));
            Map<Long, List<NbcFlupSpectData>> dataByFollowUp = spectDataResult.get()
                    .stream()
                    .map(NbcFlupSpectData::buildFromRecord)
                    .collect(toMap(NbcFlupSpectData::getNbc_followup_n, nbcFlupSpectData -> {
                        List<NbcFlupSpectData> datas = new ArrayList<>();
                        datas.add(nbcFlupSpectData);
                        return datas;
                    }, (d1, d2) -> {
                        d1.addAll(d2);
                        return d1;
                    }));
            List<SpectGridData> gridData = followup.values()
                    .stream()
                    .map(nbcFollowUp -> {
                        NbcStud nbcStud = study.get(nbcFollowUp.getNbc_stud_n());
                        if (nbcStud == null) return Optional.empty();
                        NbcPatients nbcPatients = patients.get(nbcStud.getNbc_patients_n());
                        if (nbcPatients == null) return Optional.empty();
                        NbcTarget nbcTarget = targets.get(nbcFollowUp.getNbc_target_n());
                        List<NbcFlupSpectData> datas = dataByFollowUp.get(nbcFollowUp.getN());
                        if (datas == null) return Optional.empty();
                        NbcStudInj nbcStudInj = injByStudyId.getOrDefault(nbcStud.getN(), NbcStudInj.builder().n(-1L)
                                .nbc_stud_n(nbcStud.getN())
                                .build());
                        return Optional.of(new SpectGridDBData(nbcPatients, nbcStud, nbcFollowUp, nbcStudInj, nbcTarget, datas, targets.values()).getSpectGridData());
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

    public SpectGridData getBlankSpectGridData(NbcPatients nbcPatients) {
        return new SpectGridDBData(nbcPatients, nbcTargetDao.getPatientsTargets(nbcPatients)).getSpectGridData();
    }

    public void persistSpectData(SpectGridData spectGridData) {
        SpectGridDBData spectGridDBData = spectGridData.getSpectGridDBData();
        // study
        NbcStud nbcStud = spectGridDBData.getNbcStud();
        if (nbcStud.getN() == -1) {
            nbcStud.setStudydatetime(DateUtils.asDate(spectGridData.getStudyDate()));
            nbcStudDao.createNbcStud(nbcStud);
        } else if (!spectGridData.getStudyDate().equals(DateUtils.asLocalDate(nbcStud.getStudydatetime()))) {
            nbcStudDao.updateStudy(nbcStud);
        }
        // inj
        NbcStudInj nbcStudInj = spectGridDBData.getNbcStudInj();
        if (nbcStudInj.getN() == -1) {
            nbcStudInj.setInj_activity_bq(spectGridData.getDose());
            nbcStudInj.setNbc_stud_n(nbcStud.getN());
            nbcStudInjDao.insertStudInj(nbcStudInj);
        } else if (!nbcStudInj.getInj_activity_bq().equals(spectGridData.getDose()) || !nbcStudInj.getNbc_stud_n().equals(nbcStud.getN())) {
            nbcStudInj.setInj_activity_bq(spectGridData.getDose());
            nbcStudInj.setNbc_stud_n(nbcStud.getN());
            nbcStudInjDao.updateInj(nbcStudInj);
        }
        NbcFollowUp nbcFollowUp = NbcFollowUp.builder()
                .nbc_stud_n(nbcStud.getN())
                .nbc_target_n(spectGridData.getTarget().getN())
                .build();
        List<NbcFlupSpectData> data = createData(spectGridData);
        nbcFlupSpectDataDao.createSpectFollowUpData(nbcFollowUp, data);

    }

    public void deleteSpectData(SpectGridData spectGridData) {
        SpectGridDBData spectGridDBData = spectGridData.getSpectGridDBData();
        // Нет данных.
        if (spectGridDBData.getNbcFollowUp().getN() == -1) return;
        context.transaction(configuration -> {
            nbcFollowUpDao.deleteFollowUp(spectGridDBData.getNbcFollowUp());
            nbcFlupSpectDataDao.deleteSpectData(spectGridDBData.getNbcFollowUp());
        });
    }

    private List<NbcFlupSpectData> createData(SpectGridData spectGridData) {
        NbcFlupSpectData hyp = NbcFlupSpectData.builder()
                .targetType(TargetType.HYP)
                .contourType(ContourType.SPHERE)
                .volume(spectGridData.getHypVolume())
                .early_phase(spectGridData.getHypMin30())
                .late_phase(spectGridData.getHypMin60())
                .build();
        NbcFlupSpectData hizSphere = NbcFlupSpectData.builder()
                .targetType(TargetType.HIZ)
                .contourType(ContourType.SPHERE)
                .volume(spectGridData.getHizSphereVolume())
                .early_phase(spectGridData.getHizSphereMin30())
                .late_phase(spectGridData.getHizSphereMin60())
                .build();

        NbcFlupSpectData hizIsoline10 = NbcFlupSpectData.builder()
                .targetType(TargetType.HIZ)
                .contourType(ContourType.ISOLYNE10)
                .volume(spectGridData.getHizIsoline10Volume())
                .early_phase(spectGridData.getHizIsoline10Min30())
                .late_phase(spectGridData.getHizIsoline10Min60())
                .build();

        NbcFlupSpectData hizIsoline25 = NbcFlupSpectData.builder()
                .targetType(TargetType.HIZ)
                .contourType(ContourType.ISOLYNE25)
                .volume(spectGridData.getHizIsoline25Volume())
                .early_phase(spectGridData.getHizIsoline25Min30())
                .late_phase(spectGridData.getHizIsoline25Min60())
                .build();

        NbcFlupSpectData targetSphere = NbcFlupSpectData.builder()
                .targetType(TargetType.TARGET)
                .contourType(ContourType.SPHERE)
                .volume(spectGridData.getTargetSphereVolume())
                .early_phase(spectGridData.getTargetSphereMin30())
                .late_phase(spectGridData.getTargetSphereMin60())
                .build();

        NbcFlupSpectData targetIsoline10 = NbcFlupSpectData.builder()
                .targetType(TargetType.TARGET)
                .contourType(ContourType.ISOLYNE10)
                .volume(spectGridData.getTargetIsoline10Volume())
                .early_phase(spectGridData.getTargetIsoline10Min30())
                .late_phase(spectGridData.getTargetIsoline10Min60())
                .build();

        NbcFlupSpectData targetIsoline25 = NbcFlupSpectData.builder()
                .targetType(TargetType.TARGET)
                .contourType(ContourType.ISOLYNE25)
                .volume(spectGridData.getTargetIsoline25Volume())
                .early_phase(spectGridData.getTargetIsoline25Min30())
                .late_phase(spectGridData.getTargetIsoline25Min60())
                .build();
        return Arrays.asList(hyp, hizSphere, hizIsoline10, hizIsoline25, targetSphere, targetIsoline10, targetIsoline25);
    }
}
