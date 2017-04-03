package lgk.nsbc.view.spectcrud;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class SpectGridData {
    private String name;
    private String surname;
    private String patronymic;
    private String caseHistoryNum;
    private LocalDate studyDate;
    private String targetName;

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
}
