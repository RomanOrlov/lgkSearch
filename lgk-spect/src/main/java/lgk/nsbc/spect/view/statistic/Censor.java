package lgk.nsbc.spect.view.statistic;

public enum Censor {
    CENSORED("Цензурирован"),
    NOT_CENSORED("Не цензурирован");

    private final String censorStatus;

    Censor(String censorStatus) {
        this.censorStatus = censorStatus;
    }


    @Override
    public String toString() {
        return censorStatus;
    }
}
