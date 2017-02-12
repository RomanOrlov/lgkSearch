package lgk.nsbc;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DBConnectionProperty {
    @Getter
    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.driverClassName}")
    @Getter
    private String driverClassName;

    @Value("${spring.datasource.username}")
    @Getter
    private String username;

    @Getter
    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.dbcp.max-active}")
    private String maxActive;

    public Integer getMaxActive() {
        return Integer.valueOf(maxActive);
    }
}
