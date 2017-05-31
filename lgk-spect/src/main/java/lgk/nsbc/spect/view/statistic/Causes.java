package lgk.nsbc.spect.view.statistic;

public enum Causes {
    DEATH("Смерть"),
    LAST_HAPPENED_STUDY("Последняя произошедщее исследование"),
    LAST_HAPPENED_PROCUDERE("Последняя произошедщая процедура"),
    FOLLOWUP("Контроль");

    private final String causeText;

    Causes(String causeText) {
        this.causeText = causeText;
    }


    @Override
    public String toString() {
        return causeText;
    }
}
