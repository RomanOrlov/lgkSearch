package lgk.nsbc.template.model.spect;

import lgk.nsbc.template.model.NbcFlupSpectData;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public enum TargetType {
    HIZ("Хороидальное сплетение"), TARGET("Опухоль"), HYP("Гипофиз");

    private final String name;

    TargetType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<NbcFlupSpectData>  getSublistOfTarget(List<NbcFlupSpectData> spectDatas) {
        return spectDatas.stream()
                .filter(nbcFlupSpectData -> nbcFlupSpectData.getStructure_type().equals(getName()))
                .collect(toList());
    }
}
