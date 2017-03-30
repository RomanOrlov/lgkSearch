package lgk.nsbc.view.spectflup.bind;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Класс для Binding с компонентами окна редактирования данных ОФЕКТ
 */
@Getter
@Setter
public class SpectFlupData {
    private Double dose;
    private LocalDate studyDate;
}
