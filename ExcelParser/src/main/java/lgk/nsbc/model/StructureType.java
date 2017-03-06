package lgk.nsbc.model;

public enum StructureType {
    EYE("Хориоидальное сплетение"), TARGET("Опухоль"), BRAIN("Гипофиз");

    private String name;

    StructureType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
