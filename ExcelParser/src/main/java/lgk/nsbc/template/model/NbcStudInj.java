package lgk.nsbc.template.model;

import lombok.*;

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
    private Double inj_begin;
    private Double inj_end;
}
