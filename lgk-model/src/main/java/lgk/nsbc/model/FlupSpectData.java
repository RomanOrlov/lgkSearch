package lgk.nsbc.model;

import lgk.nsbc.generated.tables.records.NbcFlupSpectDataRecord;
import lgk.nsbc.model.spect.ContourType;
import lgk.nsbc.model.spect.TargetType;
import lombok.*;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.NbcFlupSpectData.NBC_FLUP_SPECT_DATA;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlupSpectData implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private Long nbc_followup_n;

    private TargetType targetType;
    private ContourType contourType;

    private Double volume;
    private Double early_phase;
    private Double late_phase;

    public static FlupSpectData buildFromRecord(Record record) {
        return builder()
                .n(record.get(NBC_FLUP_SPECT_DATA.N))
                .nbc_followup_n(record.get(NBC_FLUP_SPECT_DATA.NBC_FOLLOWUP_N))
                .targetType(TargetType.getByDictionaryId(record.get(NBC_FLUP_SPECT_DATA.NBC_TARGET_TARGET_TYPE_N)))
                .contourType(ContourType.getByDictionaryId(record.get(NBC_FLUP_SPECT_DATA.NBC_FLUP_SPECT_CONTOURTYPE_N)))
                .volume(record.get(NBC_FLUP_SPECT_DATA.VOLUME))
                .early_phase(record.get(NBC_FLUP_SPECT_DATA.EARLY_PHASE))
                .late_phase(record.get(NBC_FLUP_SPECT_DATA.LATE_PHASE))
                .build();
    }

    public NbcFlupSpectDataRecord getRecord() {
        NbcFlupSpectDataRecord record = new NbcFlupSpectDataRecord();
        record.setN(n);
        record.setNbcFollowupN(nbc_followup_n);
        record.setNbcTargetTargetTypeN(targetType.getDictionaryId());
        record.setNbcFlupSpectContourtypeN(contourType.getDictionaryId());
        record.setVolume(volume);
        record.setEarlyPhase(early_phase);
        record.setLatePhase(late_phase);
        return record;
    }

}
