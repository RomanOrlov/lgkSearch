package lgk.nsbc.model.spect;

import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public enum MainInfo {
    VOLUME("Объем"), MIN30("30 минут"), MIN60("60 минут");

    private final String name;

    MainInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Set<String> getNames() {
        return Arrays.stream(values())
                .map(MainInfo::getName)
                .collect(toSet());
    }
}
