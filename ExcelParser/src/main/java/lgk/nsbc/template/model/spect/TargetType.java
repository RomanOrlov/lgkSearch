package lgk.nsbc.template.model.spect;

public enum TargetType {
    HIZ("Хороидальное сплетение"), TARGET("Опухоль"), HYP("Гипофиз");

    private final String name;

    TargetType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
