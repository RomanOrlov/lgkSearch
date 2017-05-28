package lgk.nsbc.model.dao;

import lgk.nsbc.generated.tables.records.FlupSpectDataRecord;
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
import static lgk.nsbc.generated.Sequences.FLUP_SPECT_DATA_N;
import static lgk.nsbc.generated.tables.FlupSpectData.FLUP_SPECT_DATA;
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
        spectDatas.forEach(data -> data.setFollowupN(followUp.getN()));
        persistSpectData(spectDatas);
    }

    public void persistSpectData(List<FlupSpectData> spectDatas) {
        InsertValuesStep7<FlupSpectDataRecord, Long, Long, Long, Long, Double, Double, Double> insertStep = context.insertInto(FLUP_SPECT_DATA)
                .columns(FLUP_SPECT_DATA.N,
                        FLUP_SPECT_DATA.FOLLOWUP_N,
                        FLUP_SPECT_DATA.TARGET_TARGET_TYPE_N,
                        FLUP_SPECT_DATA.FLUP_SPECT_CONTOURTYPE_N,
                        FLUP_SPECT_DATA.VOLUME,
                        FLUP_SPECT_DATA.EARLY_PHASE,
                        FLUP_SPECT_DATA.LATE_PHASE);
        for (FlupSpectData data : spectDatas) {
            insertStep = insertStep.values(
                    FLUP_SPECT_DATA_N.nextval(),
                    val(data.getFollowupN()),
                    val(data.getTargetType().getDictionaryId()),
                    val(data.getContourType().getDictionaryId()),
                    val(data.getVolume()),
                    val(data.getEarlyPhase()),
                    val(data.getLatePhase())
            );
        }
        insertStep.execute();
    }

    public void updateSpectData(List<FlupSpectData> spectDatas) {
        List<FlupSpectDataRecord> records = spectDatas.stream()
                .map(FlupSpectData::getRecord)
                .collect(toList());
        context.batchUpdate(records).execute();
    }

    public List<FlupSpectData> findByFollowup(FollowUp followUp) {
        Result<FlupSpectDataRecord> records = context.fetch(FLUP_SPECT_DATA, FLUP_SPECT_DATA.FOLLOWUP_N.eq(followUp.getN()));
        return records.stream()
                .map(FlupSpectData::buildFromRecord)
                .collect(toList());
    }

    public void deleteSpectData(FollowUp followUp) {
        int execute = context.deleteFrom(FLUP_SPECT_DATA)
                .where(FLUP_SPECT_DATA.FOLLOWUP_N.eq(followUp.getN()))
                .execute();
    }
}
