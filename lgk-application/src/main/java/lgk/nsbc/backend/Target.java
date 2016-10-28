package lgk.nsbc.backend;

import lgk.nsbc.generated.tables.NbcFollowup;
import lgk.nsbc.generated.tables.NbcPatients;
import lgk.nsbc.generated.tables.NbcProc;
import lgk.nsbc.generated.tables.NbcTarget;
import org.jooq.Table;
import org.jooq.TableField;

/**
 * То, что мы будем искать. Пока это Пациент, процедура, в дальнейшем может быть follow up и мишени.
 * Created by Роман on 16.05.2016.
 */
public enum Target {
    PATIENT(NbcPatients.NBC_PATIENTS,NbcPatients.NBC_PATIENTS.N,"Пациент"),
    PROCEDURE (NbcProc.NBC_PROC,NbcProc.NBC_PROC.N,"Процедура"),
    MARK(NbcTarget.NBC_TARGET,NbcTarget.NBC_TARGET.N,"Мишень"),
    FOLLOWUP (NbcFollowup.NBC_FOLLOWUP,NbcFollowup.NBC_FOLLOWUP.N,"Follow Up");

    Table lgkTable;
    TableField uniqueKey;
    String rusName;

    /**
     * @param lgkTable Таблица, к которой относится поиск той или иной цели
     * @param uniqueKey Уникальный ключ для поиска объекта (Который будет тянуться из базы ВСЕГДА)
     * @param rusName Какое русское название отображать
     */
    Target(Table lgkTable,TableField uniqueKey, String rusName) {
        this.lgkTable = lgkTable;
        this.uniqueKey = uniqueKey;
        this.rusName = rusName;
    }

    @Override
    public String toString() {
        return rusName;
    }

    public Table getLgkTable() {
        return lgkTable;
    }

    public TableField getUniqueKey() { return uniqueKey; }
}
