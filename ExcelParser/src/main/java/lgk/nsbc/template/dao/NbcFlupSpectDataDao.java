package lgk.nsbc.template.dao;

import lgk.nsbc.generated.Sequences;
import lgk.nsbc.generated.tables.records.NbcFlupSpectDataRecord;
import lgk.nsbc.template.model.NbcFlupSpectData;
import lgk.nsbc.template.model.NbcFollowUp;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep7;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static lgk.nsbc.generated.tables.NbcFlupSpectData.NBC_FLUP_SPECT_DATA;
import static org.jooq.impl.DSL.val;

@Service
public class NbcFlupSpectDataDao {
    @Autowired
    private DSLContext context;
    @Autowired
    private NbcFollowUpDao nbcFollowUpDao;

    public void createSpectFollowUpData(NbcFollowUp nbcFollowUp, List<NbcFlupSpectData> spectDatas) {
        context.transaction(configuration -> {
            nbcFollowUpDao.createFollowUp(nbcFollowUp);
            spectDatas.forEach(data -> data.setNbc_followup_n(nbcFollowUp.getN()));
            persistData(spectDatas);
        });
    }

    private void persistData(List<NbcFlupSpectData> spectDatas) {
        InsertValuesStep7<NbcFlupSpectDataRecord, Long, Long, Long, Long, Double, Double, Double> columns = context.insertInto(NBC_FLUP_SPECT_DATA)
                .columns(NBC_FLUP_SPECT_DATA.N,
                        NBC_FLUP_SPECT_DATA.NBC_FOLLOWUP_N,
                        NBC_FLUP_SPECT_DATA.NBC_TARGET_TARGET_TYPE_N,
                        NBC_FLUP_SPECT_DATA.NBC_FLUP_SPECT_CONTOURTYPE_N,
                        NBC_FLUP_SPECT_DATA.VOLUME,
                        NBC_FLUP_SPECT_DATA.EARLY_PHASE,
                        NBC_FLUP_SPECT_DATA.LATE_PHASE);
        for (NbcFlupSpectData data : spectDatas) {
            columns = columns.values(Sequences.NBC_FLUP_SPECT_DATA_N.nextval(),
                    val(data.getNbc_followup_n()),
                    val(data.getTargetType().getDictionaryId()),
                    val(data.getContourType().getDictionaryId()),
                    val(data.getVolume()),
                    val(data.getEarly_phase()),
                    val(data.getLate_phase()));
        }
        int execute = columns.execute();
    }
}
