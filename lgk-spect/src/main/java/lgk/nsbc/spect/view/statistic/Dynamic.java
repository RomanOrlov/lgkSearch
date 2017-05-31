package lgk.nsbc.spect.view.statistic;

public enum Dynamic {
    POSITIVE("пол"),
    NEGATIVE("отр"),
    STABLE("стаб");

    private final String dynamicText;

    Dynamic(String dynamicText) {
        this.dynamicText = dynamicText;
    }


    @Override
    public String toString() {
        return dynamicText;
    }
}
