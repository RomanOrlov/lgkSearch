package lgk.nsbc.dao;

import lgk.nsbc.generated.tables.records.NbcFlupSpectDataRecord;
import lgk.nsbc.generated.tables.records.NbcFollowupRecord;
import lgk.nsbc.generated.tables.records.NbcStudRecord;
import lgk.nsbc.generated.tables.records.NbcTargetRecord;
import lgk.nsbc.model.*;
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
import static lgk.nsbc.generated.tables.BasPeople.BAS_PEOPLE;
import static lgk.nsbc.generated.tables.NbcFlupSpectData.NBC_FLUP_SPECT_DATA;
import static lgk.nsbc.generated.tables.NbcFollowup.NBC_FOLLOWUP;
import static lgk.nsbc.generated.tables.NbcPatients.NBC_PATIENTS;
import static lgk.nsbc.generated.tables.NbcStud.NBC_STUD;
import static lgk.nsbc.generated.tables.NbcTarget.NBC_TARGET;

@Service
public class SpectDataManager {
    @Autowired
    private DSLContext context;

    /**
     * Этот метод должен быть чертовски быстрым.
     * Поэтому он
     * @return
     * @throws Exception
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
            CompletableFuture<Result<NbcTargetRecord>> targetsResult = context.fetchAsync(NBC_TARGET, NBC_TARGET.N.in(targetsId))
                    .toCompletableFuture();
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
                        return Optional.of(new SpectGridDBData(nbcPatients, nbcStud, nbcFollowUp, nbcTarget, datas).getSpectGridData());
                    })
                    .filter(Optional::isPresent)
                    .map(o -> (SpectGridData)(o.get()))
                    .collect(toList());
            return gridData;
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }
}
