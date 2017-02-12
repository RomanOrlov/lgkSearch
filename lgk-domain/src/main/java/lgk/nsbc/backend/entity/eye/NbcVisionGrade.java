package lgk.nsbc.backend.entity.eye;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "NBC_VISION_1_GRADE")
public class NbcVisionGrade implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "AMOUNT_FROM")
    private Double amountFrom;

    @Column(name = "AMOUNT_TO")
    private Double amountTo;

}