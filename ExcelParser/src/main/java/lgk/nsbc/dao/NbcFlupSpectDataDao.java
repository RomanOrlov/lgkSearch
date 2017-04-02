package lgk.nsbc.dao;

import lgk.nsbc.generated.tables.records.NbcFlupSpectDataRecord;
import lgk.nsbc.model.NbcFlupSpectData;
import lgk.nsbc.model.NbcFollowUp;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static lgk.nsbc.generated.tables.NbcFlupSpectData.NBC_FLUP_SPECT_DATA;

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
            persistSpectData(spectDatas);
        });
    }

    public void persistSpectData(List<NbcFlupSpectData> spectDatas) {
        context.transaction(configuration -> {
            List<NbcFlupSpectDataRecord> records = spectDatas.stream()
                    .map(NbcFlupSpectData::getRecord)
                    .collect(Collectors.toList());
            context.batchInsert(records);
        });
    }

    public void updateSpectData(List<NbcFlupSpectData> spectDatas) {
        context.transaction(configuration -> {
            List<NbcFlupSpectDataRecord> records = spectDatas.stream()
                    .map(NbcFlupSpectData::getRecord)
                    .collect(Collectors.toList());
            context.batchUpdate(records);
        });
    }

    public List<NbcFlupSpectData> findByNbcFollowup(NbcFollowUp nbcFollowUp) {
        Result<NbcFlupSpectDataRecord> records = context.fetch(NBC_FLUP_SPECT_DATA, NBC_FLUP_SPECT_DATA.NBC_FOLLOWUP_N.eq(nbcFollowUp.getN()));
        return records.stream()
                .map(NbcFlupSpectData::buildFromRecord)
                .collect(Collectors.toList());
    }
}
