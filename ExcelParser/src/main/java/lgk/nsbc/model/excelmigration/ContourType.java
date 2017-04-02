package lgk.nsbc.model.excelmigration;

public enum ContourType {
    SPHERE("Сфера"), ISOLYNE("Изолиния");

    private String name;

    ContourType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
