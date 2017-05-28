package lgk.nsbc.generated;

import org.jooq.util.Definition;
import org.jooq.util.SequenceDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.firebird.FirebirdDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс, необходимый для фильтрации таблиц, которые мне не нужны.
 */
public class CustomDatabase extends FirebirdDatabase {
    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> tables0 = super.getTables0();
        return filterDefinitions(tables0);
    }


    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> sequences0 = super.getSequences0();
        return filterDefinitions(sequences0);
    }

    private <T extends Definition> List<T> filterDefinitions(List<T> definitions) {
        Map<String, T> definitionsWithPostfix = new HashMap<String, T>();
        for (T t : definitions) {
            if (t.getName().contains("_1")) {
                definitionsWithPostfix.put(t.getName().replace("_1", ""), t);
            }
        }
        List<T> filteredDefinitions = new ArrayList<T>();
        for (T t : definitions) {
            if (!definitionsWithPostfix.containsKey(t.getName())) {
                filteredDefinitions.add(t);
            }
        }
        return filteredDefinitions;
    }
}
