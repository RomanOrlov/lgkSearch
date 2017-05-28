package lgk.nsbc.model;

import lgk.nsbc.generated.tables.records.FlupSpectDataRecord;
import lgk.nsbc.model.spect.ContourType;
import lgk.nsbc.model.spect.TargetType;
import lombok.*;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.FlupSpectData.FLUP_SPECT_DATA;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlupSpectData implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private Long followupN;

    private TargetType targetType;
    private ContourType contourType;

    private Double volume;
    private Double earlyPhase;
    private Double latePhase;

    public static FlupSpectData buildFromRecord(Record record) {
        return builder()
                .n(record.get(FLUP_SPECT_DATA.N))
                .followupN(record.get(FLUP_SPECT_DATA.FOLLOWUP_N))
                .targetType(TargetType.getByDictionaryId(record.get(FLUP_SPECT_DATA.TARGET_TARGET_TYPE_N)))
                .contourType(ContourType.getByDictionaryId(record.get(FLUP_SPECT_DATA.FLUP_SPECT_CONTOURTYPE_N)))
                .volume(record.get(FLUP_SPECT_DATA.VOLUME))
                .earlyPhase(record.get(FLUP_SPECT_DATA.EARLY_PHASE))
                .latePhase(record.get(FLUP_SPECT_DATA.LATE_PHASE))
                .build();
    }

    public FlupSpectDataRecord getRecord() {
        FlupSpectDataRecord record = new FlupSpectDataRecord();
        record.setN(n);
        record.setNbcFollowupN(followupN);
        record.setNbcTargetTargetTypeN(targetType.getDictionaryId());
        record.setNbcFlupSpectContourtypeN(contourType.getDictionaryId());
        record.setVolume(volume);
        record.setEarlyPhase(earlyPhase);
        record.setLatePhase(latePhase);
        return record;
    }

}
