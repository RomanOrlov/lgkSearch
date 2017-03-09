package lgk.nsbc.template.model;

import lombok.*;
import org.jooq.Record;

import static lgk.nsbc.generated.tables.NbcFlupSpectData.NBC_FLUP_SPECT_DATA;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NbcFlupSpectData {
    private Long n;
    private Long nbc_flup_spect_n;
    private String structure_type;
    private String contour_type;
    private Long contour_size;
    private Double volume;
    private Double early_phase;
    private Double late_phase;

    public static NbcFlupSpectData buildFromRecord(Record record) {
        return builder()
                .n(record.get(NBC_FLUP_SPECT_DATA.N))
                .nbc_flup_spect_n(record.get(NBC_FLUP_SPECT_DATA.NBC_FLUP_SPECT_N))
                .structure_type(record.get(NBC_FLUP_SPECT_DATA.STRUCTURE_TYPE))
                .contour_type(record.get(NBC_FLUP_SPECT_DATA.CONTOUR_TYPE))
                .contour_size(record.get(NBC_FLUP_SPECT_DATA.CONTOUR_SIZE))
                .volume(record.get(NBC_FLUP_SPECT_DATA.VOLUME))
                .early_phase(record.get(NBC_FLUP_SPECT_DATA.EARLY_PHASE))
                .late_phase(record.get(NBC_FLUP_SPECT_DATA.LATE_PHASE))
                .build();
    }
}
