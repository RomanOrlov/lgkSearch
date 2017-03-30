package lgk.nsbc.view.spectflup.bind;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataUnitBind {
    // Старое значение id, если оно есть
    private Long n;
    private Double volume;
    private Double earlyPhase;
    private Double latePhase;

    public DataUnitBind(Double volume, Double earlyPhase, Double latePhase) {
        this.volume = volume;
        this.earlyPhase = earlyPhase;
        this.latePhase = latePhase;
    }
}
