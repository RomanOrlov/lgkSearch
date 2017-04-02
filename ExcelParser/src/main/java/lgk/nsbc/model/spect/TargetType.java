package lgk.nsbc.model.spect;

import lgk.nsbc.model.NbcFlupSpectData;

import java.util.List;

import static java.util.stream.Collectors.toList;

public enum TargetType {
    HIZ("Хороидальное сплетение", 9L), TARGET("Опухоль", 1L), HYP("Гипофиз", 8L);

    private final String name;
    private final Long dictionaryId;

    TargetType(String name, Long dictionaryId) {
        this.name = name;
        this.dictionaryId = dictionaryId;
    }

    public String getName() {
        return name;
    }

    public long getDictionaryId() {
        return dictionaryId;
    }

    public static TargetType getByDictionaryId(Long id) {
        for (TargetType targetType : values()) {
            if (targetType.dictionaryId.equals(id))
                return targetType;
        }
        throw new RuntimeException("No such target type with id " + id);
    }

    public List<NbcFlupSpectData> getSublistOfTarget(List<NbcFlupSpectData> spectDatas) {
        return spectDatas.stream()
                .filter(nbcFlupSpectData -> nbcFlupSpectData.getTargetType() == this)
                .collect(toList());
    }
}
