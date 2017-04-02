package lgk.nsbc.model.spect;

public enum MainInfo {
    VOLUME("Объем"), MIN30("30 минут"), MIN60("60 минут");

    private final String name;

    MainInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
