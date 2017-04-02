package lgk.nsbc.model.excelmigration;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Класс, при помощи которого можно будет связать данные в excel c модельными классами базы.
 */
@Data
public class StudyRecords {
    private Long spectNumder;
    private Date date;
    private String fullName;
    private Double dose;
    private List<StudyTarget> targets;

    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        return "records{" +
                ", numder=" + spectNumder +
                ", name='" + fullName + '\'' +
                ", dose=" + dose +
                ", targets=" + targets +
                ", date=" + format.format(date) +
                '}';
    }
}
