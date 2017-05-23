package lgk.nsbc.model.dao;

import lgk.nsbc.generated.tables.records.NbcFlupSpectDataRecord;
import lgk.nsbc.model.FlupSpectData;
import lgk.nsbc.model.FollowUp;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep7;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static lgk.nsbc.generated.Sequences.NBC_FLUP_SPECT_DATA_N;
import static lgk.nsbc.generated.tables.NbcFlupSpectData.NBC_FLUP_SPECT_DATA;
import static org.jooq.impl.DSL.val;

@Service
public class FlupSpectDataDao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private DSLContext context;
    @Autowired
    private FollowUpDao followUpDao;

    public void createSpectFollowUpData(FollowUp followUp, List<FlupSpectData> spectDatas) {
        followUpDao.createFollowUp(followUp);
        spectDatas.forEach(data -> data.setNbc_followup_n(followUp.getN()));
        persistSpectData(spectDatas);
    }

    public void persistSpectData(List<FlupSpectData> spectDatas) {
        InsertValuesStep7<NbcFlupSpectDataRecord, Long, Long, Long, Long, Double, Double, Double> insertStep = context.insertInto(NBC_FLUP_SPECT_DATA)
                .columns(NBC_FLUP_SPECT_DATA.N,
                        NBC_FLUP_SPECT_DATA.NBC_FOLLOWUP_N,
                        NBC_FLUP_SPECT_DATA.NBC_TARGET_TARGET_TYPE_N,
                        NBC_FLUP_SPECT_DATA.NBC_FLUP_SPECT_CONTOURTYPE_N,
                        NBC_FLUP_SPECT_DATA.VOLUME,
                        NBC_FLUP_SPECT_DATA.EARLY_PHASE,
                        NBC_FLUP_SPECT_DATA.LATE_PHASE);
        for (FlupSpectData data : spectDatas) {
            insertStep = insertStep.values(
                    NBC_FLUP_SPECT_DATA_N.nextval(),
                    val(data.getNbc_followup_n()),
                    val(data.getTargetType().getDictionaryId()),
                    val(data.getContourType().getDictionaryId()),
                    val(data.getVolume()),
                    val(data.getEarly_phase()),
                    val(data.getLate_phase())
            );
        }
        insertStep.execute();
    }

    public void updateSpectData(List<FlupSpectData> spectDatas) {
        List<NbcFlupSpectDataRecord> records = spectDatas.stream()
                .map(FlupSpectData::getRecord)
                .collect(toList());
        context.batchUpdate(records).execute();
    }

    public List<FlupSpectData> findByNbcFollowup(FollowUp followUp) {
        Result<NbcFlupSpectDataRecord> records = context.fetch(NBC_FLUP_SPECT_DATA, NBC_FLUP_SPECT_DATA.NBC_FOLLOWUP_N.eq(followUp.getN()));
        return records.stream()
                .map(FlupSpectData::buildFromRecord)
                .collect(toList());
    }

    public void deleteSpectData(FollowUp followUp) {
        int execute = context.deleteFrom(NBC_FLUP_SPECT_DATA)
                .where(NBC_FLUP_SPECT_DATA.NBC_FOLLOWUP_N.eq(followUp.getN()))
                .execute();
    }
}
