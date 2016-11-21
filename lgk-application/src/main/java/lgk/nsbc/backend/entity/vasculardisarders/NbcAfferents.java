package lgk.nsbc.backend.entity.vasculardisarders;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "NBC_AFFERENTS_1")
@XmlRootElement
public class NbcAfferents implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @Column(name = "ACA")
    private Character aca;

    @Column(name = "MCA")
    private Character mca;

    @Column(name = "PCA")
    private Character pca;

    @Column(name = "SCA")
    private Character sca;

    @Column(name = "AICA")
    private Character aica;

    @Column(name = "PICA")
    private Character pica;

    @Column(name = "VA")
    private Character va;

    @Column(name = "SCAA")
    private Character scaa;

    @Column(name = "ICAA")
    private Character icaa;

    @Column(name = "BA")
    private Character ba;

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public Character getAca() {
        return aca;
    }

    public void setAca(Character aca) {
        this.aca = aca;
    }

    public Character getMca() {
        return mca;
    }

    public void setMca(Character mca) {
        this.mca = mca;
    }

    public Character getPca() {
        return pca;
    }

    public void setPca(Character pca) {
        this.pca = pca;
    }

    public Character getSca() {
        return sca;
    }

    public void setSca(Character sca) {
        this.sca = sca;
    }

    public Character getAica() {
        return aica;
    }

    public void setAica(Character aica) {
        this.aica = aica;
    }

    public Character getPica() {
        return pica;
    }

    public void setPica(Character pica) {
        this.pica = pica;
    }

    public Character getVa() {
        return va;
    }

    public void setVa(Character va) {
        this.va = va;
    }

    public Character getScaa() {
        return scaa;
    }

    public void setScaa(Character scaa) {
        this.scaa = scaa;
    }

    public Character getIcaa() {
        return icaa;
    }

    public void setIcaa(Character icaa) {
        this.icaa = icaa;
    }

    public Character getBa() {
        return ba;
    }

    public void setBa(Character ba) {
        this.ba = ba;
    }
}