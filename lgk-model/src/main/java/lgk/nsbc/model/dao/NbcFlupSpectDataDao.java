package lgk.nsbc.model.dao;

import lgk.nsbc.generated.tables.records.NbcFlupSpectDataRecord;
import lgk.nsbc.model.NbcFlupSpectData;
import lgk.nsbc.model.NbcFollowUp;
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
public class NbcFlupSpectDataDao implements Serializable{
    @Autowired
    private DSLContext context;
    @Autowired
    private NbcFollowUpDao nbcFollowUpDao;

    public void createSpectFollowUpData(NbcFollowUp nbcFollowUp, List<NbcFlupSpectData> spectDatas) {
        context.transaction(configuration -> {
            nbcFollowUpDao.createFollowUp(nbcFollowUp);
            spectDatas.forEach(data -> data.setNbc_followup_n(nbcFollowUp.getN()));
            persistSpectData(spectDatas);
        });
    }

    public void persistSpectData(List<NbcFlupSpectData> spectDatas) {
        context.transaction(configuration -> {
            InsertValuesStep7<NbcFlupSpectDataRecord, Long, Long, Long, Long, Double, Double, Double> insertStep = context.insertInto(NBC_FLUP_SPECT_DATA)
                    .columns(NBC_FLUP_SPECT_DATA.N,
                            NBC_FLUP_SPECT_DATA.NBC_FOLLOWUP_N,
                            NBC_FLUP_SPECT_DATA.NBC_TARGET_TARGET_TYPE_N,
                            NBC_FLUP_SPECT_DATA.NBC_FLUP_SPECT_CONTOURTYPE_N,
                            NBC_FLUP_SPECT_DATA.VOLUME,
                            NBC_FLUP_SPECT_DATA.EARLY_PHASE,
                            NBC_FLUP_SPECT_DATA.LATE_PHASE);
            for (NbcFlupSpectData data : spectDatas) {
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
        });
    }

    public void updateSpectData(List<NbcFlupSpectData> spectDatas) {
        context.transaction(configuration -> {
            List<NbcFlupSpectDataRecord> records = spectDatas.stream()
                    .map(NbcFlupSpectData::getRecord)
                    .collect(toList());
            context.batchUpdate(records).execute();
        });
    }

    public List<NbcFlupSpectData> findByNbcFollowup(NbcFollowUp nbcFollowUp) {
        Result<NbcFlupSpectDataRecord> records = context.fetch(NBC_FLUP_SPECT_DATA, NBC_FLUP_SPECT_DATA.NBC_FOLLOWUP_N.eq(nbcFollowUp.getN()));
        return records.stream()
                .map(NbcFlupSpectData::buildFromRecord)
                .collect(toList());
    }

    public void deleteSpectData(NbcFollowUp nbcFollowUp) {
        int execute = context.deleteFrom(NBC_FLUP_SPECT_DATA)
                .where(NBC_FLUP_SPECT_DATA.NBC_FOLLOWUP_N.eq(nbcFollowUp.getN()))
                .execute();
    }
}
