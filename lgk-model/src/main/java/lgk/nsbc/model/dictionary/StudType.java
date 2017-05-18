package lgk.nsbc.model.dictionary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jooq.Record;

import static lgk.nsbc.generated.tables.NbcStudStudyType.NBC_STUD_STUDY_TYPE;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudType {
    private Long n;
    private String name;
    private String text;

    public static StudType buildFromRecord(Record record) {
        return builder().n(record.get(NBC_STUD_STUDY_TYPE.N))
                .name(record.get(NBC_STUD_STUDY_TYPE.NAME))
                .text(record.get(NBC_STUD_STUDY_TYPE.TEXT))
                .build();
    }

    @Override
    public String toString() {
        return name;
    }
}
