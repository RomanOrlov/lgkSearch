package lgk.nsbc.backend;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
