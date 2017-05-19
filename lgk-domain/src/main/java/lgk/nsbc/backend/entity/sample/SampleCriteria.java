package lgk.nsbc.backend.entity.sample;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "SAMPLE_CRITERIA")
public class SampleCriteria implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer n;

    @ManyToOne
    @JoinColumn(name = "selectionId", nullable = false)
    private Sample sample;

    @Column(name = "displayedInfoName")
    private String displayedInfoName;

    @Column(name = "value")
    private String value;

    public SampleCriteria() {
    }

    public SampleCriteria(Sample sample, String displayedInfoName, String value) {
        this.displayedInfoName = displayedInfoName;
        this.sample = sample;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SampleCriteria that = (SampleCriteria) o;

        return displayedInfoName != null ? displayedInfoName.equals(that.displayedInfoName) : that.displayedInfoName == null;

    }

    @Override
    public int hashCode() {
        return displayedInfoName != null ? displayedInfoName.hashCode() : 0;
    }
}
