package lgk.nsbc.model;

import lombok.*;
import org.jooq.Record;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

import static lgk.nsbc.generated.tables.StudInj.STUD_INJ;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudInj implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private Long op_create;
    private Long studN;
    private Long rfpN;
    private Double injActivityBq;
    private Double injVolMl;
    private Date injBegin;
    private Date injEnd;

    public static Optional<StudInj> buildFromRecord(Record record) {
        if (record == null) return Optional.empty();
        return Optional.of(builder()
                .n(record.get(STUD_INJ.N))
                .op_create(record.get(STUD_INJ.OP_CREATE))
                .studN(record.get(STUD_INJ.STUD_N))
                .rfpN(record.get(STUD_INJ.RFP_N))
                .injActivityBq(record.get(STUD_INJ.INJ_ACTIVITY_BQ))
                .injVolMl(record.get(STUD_INJ.INJ_VOL_ML))
                .injBegin(record.get(STUD_INJ.INJ_BEGIN))
                .injEnd(record.get(STUD_INJ.INJ_END))
                .build());
    }
}
