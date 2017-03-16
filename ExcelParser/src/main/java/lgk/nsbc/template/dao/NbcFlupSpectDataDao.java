package lgk.nsbc.template.dao;

import lgk.nsbc.generated.Sequences;
import lgk.nsbc.generated.tables.records.NbcFlupSpectDataRecord;
import lgk.nsbc.template.model.NbcFlupSpect;
import lgk.nsbc.template.model.NbcFlupSpectData;
import lgk.nsbc.template.model.NbcFollowUp;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep8;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static lgk.nsbc.generated.tables.NbcFlupSpect.NBC_FLUP_SPECT;
import static lgk.nsbc.generated.tables.NbcFlupSpectData.NBC_FLUP_SPECT_DATA;
import static org.jooq.impl.DSL.val;

@Service
public class NbcFlupSpectDataDao {
    @Autowired
    private DSLContext context;
    @Autowired
    private NbcFlupSpectDao nbcFlupSpectDao;
    @Autowired
    private NbcFollowUpDao nbcFollowUpDao;

    public void createSpectFollowUpData(NbcFollowUp nbcFollowUp,
                                        NbcFlupSpect nbcFlupSpect,
                                        List<NbcFlupSpectData> spectDatas) {
        context.transaction(configuration -> {
            nbcFollowUpDao.createFollowUp(nbcFollowUp);
            nbcFlupSpect.setNbc_followup_n(nbcFollowUp.getN());
            nbcFlupSpectDao.createSpectFollowUp(nbcFlupSpect);
            spectDatas.forEach(data -> data.setNbc_flup_spect_n(nbcFlupSpect.getN()));
            persistData(spectDatas);
        });
    }

    private void persistData(List<NbcFlupSpectData> spectDatas) {
        InsertValuesStep8<NbcFlupSpectDataRecord, Long, Long, String, String, Long, Double, Double, Double> columns = context.insertInto(NBC_FLUP_SPECT_DATA)
                .columns(NBC_FLUP_SPECT_DATA.N,
                        NBC_FLUP_SPECT_DATA.NBC_FLUP_SPECT_N,
                        NBC_FLUP_SPECT_DATA.STRUCTURE_TYPE,
                        NBC_FLUP_SPECT_DATA.CONTOUR_TYPE,
                        NBC_FLUP_SPECT_DATA.CONTOUR_SIZE,
                        NBC_FLUP_SPECT_DATA.VOLUME,
                        NBC_FLUP_SPECT_DATA.EARLY_PHASE,
                        NBC_FLUP_SPECT_DATA.LATE_PHASE);
        for (NbcFlupSpectData data : spectDatas) {
            columns = columns.values(Sequences.NBC_FLUP_SPECT_DATA_N.nextval(),
                    val(data.getNbc_flup_spect_n()),
                    val(data.getStructure_type()),
                    val(data.getContour_type()),
                    val(data.getContour_size()),
                    val(data.getVolume()),
                    val(data.getEarly_phase()),
                    val(data.getLate_phase()));
        }
        int execute = columns.execute();
        /*Result<NbcFlupSpectDataRecord> records = columns.returning(NBC_FLUP_SPECT_DATA.N)
                .fetch();
        for (int i = 0; i < records.size(); i++) {
            spectDatas.get(i).setN(records.get(i).getN());
        }*/
    }

    public List<NbcFlupSpectData> findBySpectFlup(NbcFlupSpect nbcFlupSpect) {
        Result<NbcFlupSpectDataRecord> result = context.fetch(NBC_FLUP_SPECT_DATA, NBC_FLUP_SPECT_DATA.NBC_FLUP_SPECT_N.eq(nbcFlupSpect.getN()));
        return result.stream()
                .map(NbcFlupSpectData::buildFromRecord)
                .collect(Collectors.toList());
    }

    public void deleteByNbcFlupSpectId(List<Long> nbcFlupSpectN) {
        context.transaction(configuration -> {
            int numberOfDataDeletedRecords = context.deleteFrom(NBC_FLUP_SPECT_DATA)
                    .where(NBC_FLUP_SPECT_DATA.NBC_FLUP_SPECT_N.in(nbcFlupSpectN))
                    .execute();

            int numberOfDeletedRecords = context.deleteFrom(NBC_FLUP_SPECT)
                    .where(NBC_FLUP_SPECT.N.in(nbcFlupSpectN))
                    .execute();
        });
    }
}
