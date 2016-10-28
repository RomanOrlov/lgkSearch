package lgk.nsbc.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "NBC_ORGANIZATIONS")
public class NbcOrganizations implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @Column(name = "OP_CREATE")
    private Integer opCreate;

    @Column(name = "NAME")
    private String name;

    @Column(name = "JUR_ADDRESS")
    private String jurAddress;

    @Column(name = "BAS_ADDRESES_N")
    private Integer basAddresesN;

    @Column(name = "INN")
    private String inn;

    @Column(name = "KPP")
    private String kpp;

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "BANK_PLACE")
    private String bankPlace;

    @Column(name = "BANK_BIK")
    private String bankBik;

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

    public NbcOrganizations() {
    }

    public NbcOrganizations(Integer n) {
        this.n = n;
    }

    public NbcOrganizations(Integer n, Integer opCreate) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJurAddress() {
        return jurAddress;
    }

    public void setJurAddress(String jurAddress) {
        this.jurAddress = jurAddress;
    }

    public Integer getBasAddresesN() {
        return basAddresesN;
    }

    public void setBasAddresesN(Integer basAddresesN) {
        this.basAddresesN = basAddresesN;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getKpp() {
        return kpp;
    }

    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankPlace() {
        return bankPlace;
    }

    public void setBankPlace(String bankPlace) {
        this.bankPlace = bankPlace;
    }

    public String getBankBik() {
        return bankBik;
    }

    public void setBankBik(String bankBik) {
        this.bankBik = bankBik;
    }

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public String getBaseDocument() {
        return baseDocument;
    }

    public void setBaseDocument(String baseDocument) {
        this.baseDocument = baseDocument;
    }

    public Character getIsPayRecipient() {
        return isPayRecipient;
    }

    public void setIsPayRecipient(Character isPayRecipient) {
        this.isPayRecipient = isPayRecipient;
    }

    public Integer getNbcOrganizationsN() {
        return nbcOrganizationsN;
    }

    public void setNbcOrganizationsN(Integer nbcOrganizationsN) {
        this.nbcOrganizationsN = nbcOrganizationsN;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getOrd() {
        return ord;
    }

    public void setOrd(Integer ord) {
        this.ord = ord;
    }
}

