package lgk.nsbc.spect.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static lgk.nsbc.model.spect.ContourType.*;
import static lgk.nsbc.model.spect.TargetType.*;

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
        targetList.add(new Target(HIZ, 6, SPHERE));
        targetList.add(new Target(HIZ, 10, ISOLYNE10));
        targetList.add(new Target(HIZ, 13, ISOLYNE25));
        targetList.add(new Target(TARGET, 16, SPHERE));
        targetList.add(new Target(TARGET, 20, ISOLYNE10));
        targetList.add(new Target(TARGET, 23, ISOLYNE25));
        targetList.add(new Target(HYP, 29, SPHERE));
    }
}
