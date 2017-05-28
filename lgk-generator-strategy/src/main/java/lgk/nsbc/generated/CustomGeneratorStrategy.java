package lgk.nsbc.generated;

import org.jooq.util.DefaultGeneratorStrategy;
import org.jooq.util.Definition;

public class CustomGeneratorStrategy extends DefaultGeneratorStrategy {
    /**
     * Переименование имени таблицы.
     *
     * @param definition
     * @param mode
     * @return
     */
    @Override
    public String getJavaClassName(Definition definition, Mode mode) {
        String javaClassName = super.getJavaClassName(definition, mode);
        return clearClassNameFromPrefix(javaClassName);
        //return javaClassName;
    }

    private String clearClassNameFromPrefix(String javaClassName) {
        return javaClassName.replace("_1", "")
                .replace("Nbc", "")
                .replace("Bas", "");
    }

    /**
     * Переименование того, какое будет имя у public static final
     * переменной таблицы
     *
     * @param definition
     * @return
     */
    @Override
    public String getJavaIdentifier(Definition definition) {
        String javaIdentifier = super.getJavaIdentifier(definition);
        return clearIdentifierFromPrefix(javaIdentifier);
    }

    private String clearIdentifierFromPrefix(String javaIdentifier) {
        return javaIdentifier.replace("_1", "")
                .replace("NBC_", "")
                .replace("BAS_", "");
    }
}
