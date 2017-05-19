package lgk.nsbc.backend.entity.sample;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "SAMPLE")
public class Sample implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer n;

    @Column(name = "userId")
    private Integer userId;

    @Column(name = "name")
    private String name;

    @Column(name = "comments")
    private String comments;

    @Column(name = "searchTarget")
    private String searchTarget;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sample", cascade = CascadeType.ALL)
    private Set<SampleData> sampleDataList = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sample", cascade = CascadeType.ALL)
    private Set<SampleCriteria> sampleCriteriaList = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sample", cascade = CascadeType.ALL)
    private Set<SampleDisplayedInfo> sampleDisplayedInfoList = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sample sample = (Sample) o;

        return n != null ? n.equals(sample.n) : sample.n == null;

    }

    @Override
    public int hashCode() {
        return n != null ? n.hashCode() : 0;
    }

}
