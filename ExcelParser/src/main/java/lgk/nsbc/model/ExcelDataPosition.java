package lgk.nsbc.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static lgk.nsbc.model.StructureType.*;

@Data
@Component
public class ExcelDataPosition {
    private Integer dataStart = 3;
    private Integer spectNumber = 0;
    private Integer date = 1;
    private Integer fullName = 3;
    private Integer diagnosis = 4;
    private Integer dose = 5;
    private List<Target> targetList = new ArrayList<>();

    {
        targetList.add(new Target(1, EYE, 6, ContourType.SPHERE));
        targetList.add(new Target(10, EYE, 10, ContourType.ISOLYNE));
        targetList.add(new Target(25, EYE, 13, ContourType.ISOLYNE));
        targetList.add(new Target(1, TARGET, 16, ContourType.SPHERE));
        targetList.add(new Target(10, TARGET, 20, ContourType.ISOLYNE));
        targetList.add(new Target(25, TARGET, 23, ContourType.ISOLYNE));
        targetList.add(new Target(1, BRAIN, 29, ContourType.SPHERE));
    }
}
