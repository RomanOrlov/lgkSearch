package lgk.nsbc.backend;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class AppConfig {
    @Bean
    @Autowired
    public DataSource dataSource(DBConnectionProperty connectionProperty) {
        DataSource dataSource = new DataSource();
        dataSource.setDriverClassName(connectionProperty.getDriverClassName());
        dataSource.setMaxActive(connectionProperty.getMaxActive());
        dataSource.setUsername(connectionProperty.getUsername());
        dataSource.setPassword(connectionProperty.getPassword());
        dataSource.setUrl(connectionProperty.getUrl());
        return dataSource;
    }

    @Bean
    @Autowired
    public DSLContext dslContext(DataSource dataSource) throws SQLException {
        return DSL.using(dataSource, SQLDialect.FIREBIRD_2_5);
    }
}
