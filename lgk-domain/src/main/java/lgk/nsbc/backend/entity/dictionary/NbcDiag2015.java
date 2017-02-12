package lgk.nsbc.backend.entity.dictionary;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Основная таблица диагнозов почти вче, что лечится в радиологии НИИ Бурденко
 */
@Setter
@Getter
@Entity
@Table(name = "NBC_DIAG_2015")
public class NbcDiag2015 implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "N")
    private Integer n;

    @Column(name = "UP_N")
    private Integer upN;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "SHORTS")
    private String shorts;

    @Column(name = "SHORTS_RU")
    private String shortsRu;

    @Column(name = "ORD")
    private Integer ord;

    @Column(name = "USEFUL")
    private Integer useful;

    @Column(name = "DESCRIPTION")
    private String description;

}