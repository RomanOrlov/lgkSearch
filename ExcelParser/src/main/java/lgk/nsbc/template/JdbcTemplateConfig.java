package lgk.nsbc.template;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.SQLException;

@Configuration
public class JdbcTemplateConfig {
    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        NamedParameterJdbcTemplate NamedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        return NamedParameterJdbcTemplate;
    }
}
