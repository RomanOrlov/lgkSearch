package lgk.nsbc.backend.entity.sample;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Bean для данных выборки
 */
@Setter
@Getter
@Entity
@Table(name = "SAMPLE_DATA")
public class SampleData implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer n;

    @ManyToOne
    @JoinColumn(name = "selectionId", nullable = false)
    private Sample sample;

    @Column(name = "objectUniqueId")
    private Integer objectUniqueId;

    public SampleData() {
    }

    public SampleData(Sample sample, Integer objectUniqueId) {
        this.sample = sample;
        this.objectUniqueId = objectUniqueId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof SampleData)) {
            return false;
        }
        SampleData sampleData = (SampleData) obj;
        // 2 объекта выборка эквиваленты, если они принадлежат одной выборке и обладают одинаковым Id
        return this.sample.equals(sampleData.getSample()) &&
                this.getObjectUniqueId().equals(sampleData.getObjectUniqueId());
    }

    @Override
    public int hashCode() {
        int result = n != null ? n.hashCode() : 0;
        result = 31 * result + (sample != null ? sample.hashCode() : 0);
        result = 31 * result + (objectUniqueId != null ? objectUniqueId.hashCode() : 0);
        return result;
    }

}
