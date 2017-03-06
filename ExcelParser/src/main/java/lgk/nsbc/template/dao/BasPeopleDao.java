package lgk.nsbc.template.dao;

import lgk.nsbc.template.model.BasPeople;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import static lgk.nsbc.template.model.BasPeople.Props.*;

@Service
public class BasPeopleDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<BasPeople> getPeoplesBySurname(Set<String> surname) {
        MapSqlParameterSource parameters = new MapSqlParameterSource("surnames", surname);
        String sql = "SELECT * FROM BAS_PEOPLE WHERE surname IN (:surnames)";
        return jdbcTemplate.query(sql, surname.toArray(), new RowBasPeopleMapper());
    }

    public static class RowBasPeopleMapper implements RowMapper<BasPeople> {
        @Override
        public BasPeople mapRow(ResultSet rs, int rowNum) throws SQLException {
            BasPeople basPeople = new BasPeople();
            BasPeople.builder()
                    .n(rs.getLong(n.toString()))
                    .name(rs.getString(name.toString()))
                    .surname(rs.getString(surname.toString()))
                    .patronymic(rs.getString(patronymic.toString()))
                    .sex(rs.getString(sex.toString()))
                    .birthday(rs.getDate(birthday.toString()))
                    .build();
            return basPeople;
        }
    }
}
