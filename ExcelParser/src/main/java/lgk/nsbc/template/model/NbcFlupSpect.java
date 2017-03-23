package lgk.nsbc.template.model;

import lombok.*;
import org.jooq.Record;

import java.util.Optional;

import static lgk.nsbc.generated.tables.NbcFlupSpect.NBC_FLUP_SPECT;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NbcFlupSpect {
    private Long n;
    private Long nbc_followup_n;
    private Long spect_num;
    private String diagnosis;

    public static Optional<NbcFlupSpect> buildFromRecord(Record record) {
        if (record==null) return Optional.empty();
        return Optional.of(
                builder()
                        .n(record.get(NBC_FLUP_SPECT.N))
                        .nbc_followup_n(record.get(NBC_FLUP_SPECT.NBC_FOLLOWUP_N))
                        .spect_num(record.get(NBC_FLUP_SPECT.SPECT_NUM))
                        .diagnosis(record.get(NBC_FLUP_SPECT.DIAGNOSIS))
                        .build()
        );
    }
}
