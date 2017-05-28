package lgk.nsbc.model.dvh;

import lgk.nsbc.model.dictionary.DvhDataVersion;
import lgk.nsbc.model.dictionary.DvhDoseAlgorithm;
import lgk.nsbc.model.dictionary.DvhLgpVersion;
import lombok.*;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.Dvh.DVH;
import static lgk.nsbc.model.dao.dictionary.DvhDataVersionDao.getDvhDataVersionMap;
import static lgk.nsbc.model.dao.dictionary.DvhDoseAlgorithmDao.getDvhDoseAlgorithmMap;
import static lgk.nsbc.model.dao.dictionary.DvhLgpVersionDao.getDvhLgpVersionMap;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Dvh implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long n;
    private String name;
    private Double doseMax;
    private Double doseMin;
    private DvhDataVersion dvhDataVersion;
    private DvhLgpVersion dvhLgpVersion;
    private DvhDoseAlgorithm dvhDoseAlgorithm;
    private Integer nBins;
    private Double binSize;

    public static Dvh buildFromRecord(Record record) {
        return builder().n(record.get(DVH.N))
                .name(record.get(DVH.NAME))
                .doseMax(record.get(DVH.DOSEMAX))
                .doseMin(record.get(DVH.DOSEMIN))
                .dvhDataVersion(getDvhDataVersionMap().get(record.get(DVH.DATA_VERSION)))
                .dvhLgpVersion(getDvhLgpVersionMap().get(record.get(DVH.LGP_VERSION)))
                .dvhDoseAlgorithm(getDvhDoseAlgorithmMap().get(record.get(DVH.DOSE_ALGORITHM)))
                .nBins(record.get(DVH.N_BINS))
                .binSize(record.get(DVH.BIN_SIZE))
                .build();
    }
}
