package lgk.nsbc.model.spect;

import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public enum ContourType {
    SPHERE("Сфера", 0L), ISOLYNE10("Изолиния 10", 1L), ISOLYNE25("Изолиния 25", 2L);

    private final String name;
    private final Long dictionaryId;

    ContourType(String name, Long dictionaryId) {
        this.name = name;
        this.dictionaryId = dictionaryId;
    }

    public String getName() {
        return name;
    }

    public static ContourType getByDictionaryId(Long id) {
        for (ContourType contourType : values()) {
            if (contourType.dictionaryId.equals(id))
                return contourType;
        }
        throw new RuntimeException("No such contour type with id " + id);
    }

    public Long getDictionaryId() {
        return dictionaryId;
    }

    public static Set<String> getNames() {
        return Arrays.stream(values())
                .map(ContourType::getName)
                .collect(toSet());
    }
}
