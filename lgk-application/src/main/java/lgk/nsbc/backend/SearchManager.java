package lgk.nsbc.backend;

import org.jooq.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


/**
 * Класс, отвечающий за формирование SQL запроса и получение результатов поиска из базы.
 * Created by Роман on 10.05.2016.
 */
public class SearchManager {
    /**
     * @param whereToSearch           Цель поиска - Пациент, процедура, Мишень или Follow Up
     * @param whereCondition          Одно Большое условие, критерий поиска, выбранный пользователем.
     * @param view                    Выбранные столбцы таблицы для отображения (реально существующие столбцы)
     *                                а также агрегаты. Необходимо чтобы агрегаты стояли в конце.
     * @param joinTablesAndConditions Словарь для таблиц, необходимых для JOIN ON
     * @return Результат запроса
     * @throws SQLException
     */
    public RequestMetaInfo getResult(
            Target whereToSearch,
            Condition whereCondition,
            List<SelectField<?>> view,
            Map<Table, Condition> joinTablesAndConditions
    ) throws SQLException {
        // Все действия по сборке входных параметров вынесены в презентер, тут происходит только сборка запроса.
        try (DSLContext context = DB.getDSLContext()) {
            Result<Record> result;
            SelectJoinStep<Record> selectFrom = context
                    .select(view)
                    .from(whereToSearch.getLgkTable());
            // Ставим условия на join
            if (!joinTablesAndConditions.isEmpty()) {
                for (Map.Entry<Table, Condition> entry : joinTablesAndConditions.entrySet()) {
                    selectFrom = selectFrom.leftJoin(entry.getKey()).on(entry.getValue());
                }
            }
            String sql;
            if (whereCondition != null) {
                SelectConditionStep<Record> recordSelectOnStep = selectFrom.where(whereCondition);
                sql = recordSelectOnStep.toString();
                result = recordSelectOnStep.fetch();
            } else {
                result = selectFrom.fetch();
                sql = selectFrom.toString();
            }
            return new RequestMetaInfo(result,sql);
        }
    }

    public static class RequestMetaInfo {
        private final Result<Record> records;
        private final String sql;

        public RequestMetaInfo(Result<Record> records, String sql) {
            this.records = records;
            this.sql = sql;
        }

        public Result<Record> getRecords() {
            return records;
        }

        public String getSql() {
            return sql;
        }
    }
}