package lgk.nsbc.spect.model;

import lgk.nsbc.model.spect.TargetType;
import lgk.nsbc.model.spect.ContourType;
import lombok.Data;

@Data
public class Target {
    private int dataStart;

    private final TargetType targetType;
    private final ContourType contourType;
    private Double volume;
    private Double countsAfter30min;
    private Double countsAfter60min;

    public Target(TargetType targetType, int dataStart, ContourType contourType) {
        this.targetType = targetType;
        this.dataStart = dataStart;
        this.contourType = contourType;
    }

    public Target(Target target) {
        this.targetType = target.targetType;
        this.contourType = target.contourType;
    }

    @Override
    public String toString() {
        return "Target{" +
                "contour=" + contourType +
                ", structure=" + targetType +
                ", volume=" + volume +
                ", 30min=" + countsAfter30min +
                ", 60min=" + countsAfter60min +
                '}';
    }
}
