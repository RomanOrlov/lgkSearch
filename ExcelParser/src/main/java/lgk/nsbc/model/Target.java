package lgk.nsbc.model;

import lombok.Data;

@Data
public class Target {
    private int dataStart;

    private final StructureType structureType;
    private final ContourType contourType;
    private final int contourSize;
    private Double volume;
    private Double countsAfter30min;
    private Double countsAfter60min;

    public Target(int contourSize, StructureType structureType, int dataStart, ContourType contourType) {
        this.contourSize = contourSize;
        this.structureType = structureType;
        this.dataStart = dataStart;
        this.contourType = contourType;
    }

    public Target(Target target) {
        this.structureType = target.structureType;
        this.contourType = target.contourType;
        this.contourSize = target.contourSize;
    }

    @Override
    public String toString() {
        return "Target{" +
                "contour=" + contourType +
                ", structure=" + structureType +
                ", volume=" + volume +
                ", 30min=" + countsAfter30min +
                ", 60min=" + countsAfter60min +
                '}';
    }
}
