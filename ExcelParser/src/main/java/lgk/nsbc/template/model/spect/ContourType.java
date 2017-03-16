package lgk.nsbc.template.model.spect;

import lgk.nsbc.template.model.NbcFlupSpectData;

import java.util.List;
import java.util.stream.Collectors;

public enum ContourType {
    SPHERE("Сфера"), ISOLYNE10("Изолиния 10"), ISOLYNE25("Изолиния 25");

    private final String name;

    ContourType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public NbcFlupSpectData getDataOfContour(List<NbcFlupSpectData> spectDatas) {
        return spectDatas.stream()
                .filter(nbcFlupSpectData -> nbcFlupSpectData.getContour_type().equals(getName()))
                .findFirst().orElseThrow(RuntimeException::new);
    }
}
