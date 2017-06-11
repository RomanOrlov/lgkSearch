package lgk.nsbc.model.dictionary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.KarnofskyGrade.KARNOFSKY_GRADE;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KarnofskyGrade implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private String name;
    private String text;
    private Integer amount;

    public static KarnofskyGrade buildFromRecord(Record record) {
        return builder().n(record.get(KARNOFSKY_GRADE.N))
                .name(record.get(KARNOFSKY_GRADE.NAME))
                .text(record.get(KARNOFSKY_GRADE.TEXT))
                .amount(record.get(KARNOFSKY_GRADE.AMOUNT))
                .build();
    }

    @Override
    public String toString() {
        return text;
    }
}
