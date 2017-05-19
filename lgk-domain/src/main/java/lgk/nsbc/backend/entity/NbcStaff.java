package lgk.nsbc.backend.entity;

import lgk.nsbc.backend.entity.dictionary.NbcOrganizations;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "NBC_STAFF")
public class NbcStaff implements Serializable {
    private static final long serialVersionUID = 1L;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_ORGANIZATIONS_N")
    private NbcOrganizations nbcOrganizations;

    @OneToOne
    @JoinColumn(name = "BAS_PEOPLE_N")
    private BasPeople basPeople;

    @Id
    @Column(name = "N")
    private Integer n;

    @Column(name = "POST")
    private String post;

    @Column(name = "CONTACT")
    private String contact;

    @Column(name = "NOTES")
    private String notes;

}