package lgk.nsbc.template.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class NbcTargetDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
}
