package lgk.nsbc.template.model;

import lombok.*;
import org.jooq.Record;

import java.util.Date;

import static lgk.nsbc.generated.tables.NbcStudInj.NBC_STUD_INJ;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NbcStudInj {
    private Long n;
    private Long op_create;
    private Long nbc_stud_n;
    private Long nbc_rfp_n;
    private Double inj_activity_bq;
    private Double inj_vol_ml;
    private Date inj_begin;
    private Date inj_end;

    public static NbcStudInj buildFromRecord(Record record) {
        return builder()
                .n(record.get(NBC_STUD_INJ.N))
                .op_create(record.get(NBC_STUD_INJ.OP_CREATE))
                .nbc_stud_n(record.get(NBC_STUD_INJ.NBC_STUD_N))
                .nbc_rfp_n(record.get(NBC_STUD_INJ.NBC_RFP_N))
                .inj_activity_bq(record.get(NBC_STUD_INJ.INJ_ACTIVITY_BQ))
                .inj_vol_ml(record.get(NBC_STUD_INJ.INJ_VOL_ML))
                .inj_begin(record.get(NBC_STUD_INJ.INJ_BEGIN))
                .inj_end(record.get(NBC_STUD_INJ.INJ_END))
                .build();
    }
}
