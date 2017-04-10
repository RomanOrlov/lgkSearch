package lgk.nsbc.view.spectcrud;

import lgk.nsbc.model.NbcTarget;
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
    private String caseHistoryNum;
    private Double dose;
    private LocalDate studyDate;
    private NbcTarget target;

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

    public SpectGridData(SpectGridDBData spectGridDBData) {
        this.spectGridDBData = spectGridDBData;
    }

    public Double getInEarly() {
        if (targetSphereMin30 == null || hizSphereMin30 == null) return null;
        return targetSphereMin30 / hizSphereMin30;
    }

    public Double getInLate() {
        if (targetSphereMin60 == null || hizSphereMin60 == null) return null;
        return targetSphereMin60 / hizSphereMin60;
    }

    public Double getInOut() {
        Double inEarly = getInEarly();
        Double inLate = getInLate();
        if (inEarly == null || inLate == null) return null;
        return inEarly - inLate;
    }
}
