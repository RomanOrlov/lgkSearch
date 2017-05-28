package lgk.nsbc.model.dictionary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.StudStudyType.STUD_STUDY_TYPE;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudType implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private String name;
    private String text;

    public static StudType buildFromRecord(Record record) {
        return builder().n(record.get(STUD_STUDY_TYPE.N))
                .name(record.get(STUD_STUDY_TYPE.NAME))
                .text(record.get(STUD_STUDY_TYPE.TEXT))
                .build();
    }

    @Override
    public String toString() {
        return name;
    }
}
