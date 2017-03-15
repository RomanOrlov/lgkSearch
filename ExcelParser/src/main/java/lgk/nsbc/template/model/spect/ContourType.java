package lgk.nsbc.template.model.spect;

public enum ContourType {
    SPHERE("Сфера"), ISOLYNE10("Изолиния 10"), ISOLYNE25("Изолиния 25");

    private final String name;

    ContourType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
