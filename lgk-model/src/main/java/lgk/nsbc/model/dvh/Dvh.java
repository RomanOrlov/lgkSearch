package lgk.nsbc.model.dvh;

import lgk.nsbc.model.dictionary.DvhDataVersion;
import lgk.nsbc.model.dictionary.DvhDoseAlgorithm;
import lgk.nsbc.model.dictionary.DvhLgpVersion;
import lombok.*;
import org.jooq.Record;

import java.io.Serializable;

import static lgk.nsbc.generated.tables.NbcDvh.NBC_DVH;
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
        return builder().n(record.get(NBC_DVH.N))
                .name(record.get(NBC_DVH.NAME))
                .doseMax(record.get(NBC_DVH.DOSEMAX))
                .doseMin(record.get(NBC_DVH.DOSEMIN))
                .dvhDataVersion(getDvhDataVersionMap().get(record.get(NBC_DVH.DATA_VERSION)))
                .dvhLgpVersion(getDvhLgpVersionMap().get(record.get(NBC_DVH.LGP_VERSION)))
                .dvhDoseAlgorithm(getDvhDoseAlgorithmMap().get(record.get(NBC_DVH.DOSE_ALGORITHM)))
                .nBins(record.get(NBC_DVH.N_BINS))
                .binSize(record.get(NBC_DVH.BIN_SIZE))
                .build();
    }
}
