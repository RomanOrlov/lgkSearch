package lgk.nsbc.spect.view.spectcrud;

import lgk.nsbc.model.Target;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Класс представления данных в Grid
 */
@Setter
@Getter
public class SpectGridData {
    private final SpectGridDBData spectGridDBData;
    private String name;
    private String surname;
    private String patronymic;
    private Double dose;
    private LocalDate studyDate;
    private Target target;

    // Гипофиз
    private Double hypVolume;
    private Double hypMin30;
    private Double hypMin60;

    // Хиазма
    //Сфера
    private Double hizSphereVolume;
    private Double hizSphereMin30;
    private Double hizSphereMin60;
    //Изолиния 10
    private Double hizIsoline10Volume;
    private Double hizIsoline10Min30;
    private Double hizIsoline10Min60;
    //Изолиния 25
    private Double hizIsoline25Volume;
    private Double hizIsoline25Min30;
    private Double hizIsoline25Min60;
    //Изолиния 50
    private Double hizIsoline50Volume;
    private Double hizIsoline50Min30;
    private Double hizIsoline50Min60;

    // Опухоль
    //Сфера
    private Double targetSphereVolume;
    private Double targetSphereMin30;
    private Double targetSphereMin60;
    //Изолиния 10
    private Double targetIsoline10Volume;
    private Double targetIsoline10Min30;
    private Double targetIsoline10Min60;
    //Изолиния 25
    private Double targetIsoline25Volume;
    private Double targetIsoline25Min30;
    private Double targetIsoline25Min60;
    //Изолиния 50
    private Double targetIsoline50Volume;
    private Double targetIsoline50Min30;
    private Double targetIsoline50Min60;

    public SpectGridData(SpectGridDBData spectGridDBData) {
        this.spectGridDBData = spectGridDBData;
    }

    public Double getInSphereEarly() {
        if (targetSphereMin30 == null || hizSphereMin30 == null) return null;
        return targetSphereMin30 / hizSphereMin30;
    }

    public Double getInSphereLate() {
        if (targetSphereMin60 == null || hizSphereMin60 == null) return null;
        return targetSphereMin60 / hizSphereMin60;
    }

    public Double getInSphereOut() {
        Double inEarly = getInSphereEarly();
        Double inLate = getInSphereLate();
        if (inEarly == null || inLate == null) return null;
        return inEarly - inLate;
    }

    public Double getInIsoline10Early() {
        if (targetIsoline10Min30 == null || hizIsoline10Min30 == null) return null;
        return targetIsoline10Min30 / hizIsoline10Min30;
    }

    public Double getInIsoline10Late() {
        if (targetIsoline10Min60 == null || hizIsoline10Min60 == null) return null;
        return targetIsoline10Min60 / hizIsoline10Min60;
    }

    public Double getInIsoline10Out() {
        Double inEarly = getInIsoline10Early();
        Double inLate = getInIsoline10Late();
        if (inEarly == null || inLate == null) return null;
        return inEarly - inLate;
    }

    public Double getInIsoline25Early() {
        if (targetIsoline25Min30 == null || hizIsoline25Min30 == null) return null;
        return targetIsoline25Min30 / hizIsoline25Min30;
    }

    public Double getInIsoline25Late() {
        if (targetIsoline25Min60 == null || hizIsoline25Min60 == null) return null;
        return targetIsoline25Min60 / hizIsoline25Min60;
    }

    public Double getInIsoline25Out() {
        Double inEarly = getInIsoline25Early();
        Double inLate = getInIsoline25Late();
        if (inEarly == null || inLate == null) return null;
        return inEarly - inLate;
    }

    public Double getInIsoline50Early() {
        if (targetIsoline50Min30 == null || hizIsoline50Min30 == null) return null;
        return targetIsoline50Min30 / hizIsoline50Min30;
    }

    public Double getInIsoline50Late() {
        if (targetIsoline50Min60 == null || hizIsoline50Min60 == null) return null;
        return targetIsoline50Min60 / hizIsoline50Min60;
    }

    public Double getInIsoline50Out() {
        Double inEarly = getInIsoline50Early();
        Double inLate = getInIsoline50Late();
        if (inEarly == null || inLate == null) return null;
        return inEarly - inLate;
    }

    @Override
    public String toString() {
        return surname + " " + name + " " + patronymic;
    }
}
