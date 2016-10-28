package lgk.nsbc.backend.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "NBC_STAFF")
public class NbcStaff implements Serializable {
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_ORGANIZATIONS_N")
    @PrimaryKeyJoinColumn
    private NbcOrganizations nbcOrganizations;

    @OneToOne
    @JoinColumn(name = "BAS_PEOPLE_N")
    @PrimaryKeyJoinColumn
    private BasPeople basPeople;

    @Id
    @Column(name = "N")
    private Integer n;

    @Column(name = "OP_CREATE")
    private Integer opCreate;

    @Column(name = "POST")
    private String post;

    @Column(name = "CONTACT")
    private String contact;

    @Column(name = "NOTES")
    private String notes;

    public NbcStaff() {
    }

    public NbcStaff(Integer n) {
        this.n = n;
    }

    public NbcStaff(Integer n, Integer opCreate) {
        this.n = n;
        this.opCreate = opCreate;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Integer getOpCreate() {
        return opCreate;
    }

    public void setOpCreate(Integer opCreate) {
        this.opCreate = opCreate;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public NbcOrganizations getNbcOrganizations() {
        return nbcOrganizations;
    }

    public void setNbcOrganizations(NbcOrganizations nbcOrganizations) {
        this.nbcOrganizations = nbcOrganizations;
    }

    public BasPeople getBasPeople() {
        return basPeople;
    }

    public void setBasPeople(BasPeople basPeople) {
        this.basPeople = basPeople;
    }
}
