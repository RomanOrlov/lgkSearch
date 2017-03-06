package lgk.nsbc.backend.entity.dictionary;

import lgk.nsbc.backend.entity.NbcStaff;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "NBC_ORGANIZATIONS")
public class NbcOrganizations implements Serializable {
    private static final long serialVersionUID = 1L;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "NBC_ORGANIZATIONS_N")
    private List<NbcStaff> nbcStaffList;

    @Id
    @Column(name = "N")
    private Integer n;

    @Column(name = "NAME")
    private String name;

    @Column(name = "JUR_ADDRESS")
    private String jurAddress;

    @Column(name = "BAS_ADDRESES_N")
    private Integer basAddresesN;

    @Column(name = "SIGNER")
    private String signer;

    @Column(name = "BASE_DOCUMENT")
    private String baseDocument;

    @Column(name = "IS_PAY_RECIPIENT")
    private Character isPayRecipient;

    @Column(name = "NBC_ORGANIZATIONS_N")
    private Integer nbcOrganizationsN;

    @Column(name = "SIGN")
    private String sign;

    @Column(name = "ORD")
    private Integer ord;

    @Override
    public String toString() {
        return "NbcOrganizations{" +
                "n=" + n +
                ", name='" + name + '\'' +
                ", jurAddress='" + jurAddress + '\'' +
                ", basAddresesN=" + basAddresesN +
                ", signer='" + signer + '\'' +
                ", baseDocument='" + baseDocument + '\'' +
                ", isPayRecipient=" + isPayRecipient +
                ", nbcOrganizationsN=" + nbcOrganizationsN +
                ", sign='" + sign + '\'' +
                ", ord=" + ord +
                '}';
    }

}