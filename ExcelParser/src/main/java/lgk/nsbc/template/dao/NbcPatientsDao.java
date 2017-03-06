package lgk.nsbc.template.dao;

import lgk.nsbc.template.model.BasPeople;
import lgk.nsbc.template.model.NbcPatients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static lgk.nsbc.template.model.NbcPatients.Props.*;

@Service
public class NbcPatientsDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Optional<NbcPatients> getPatientByBasPeople(BasPeople basPeople) {
        MapSqlParameterSource parameters = new MapSqlParameterSource("id", basPeople.getN());
        String sql = "SELECT * FROM NBC_PATIENTS " +
                "LEFT JOIN BAS_PEOPLE ON NBC_PATIENTS.BAS_PEOPLE_N = BAS_PEOPLE.N " +
                "WHERE NBC_PATIENTS.BAS_PEOPLE_N = :id";
        List<NbcPatients> patients = namedParameterJdbcTemplate.query(sql, parameters, new RowNbcPatientsMapper());
        if (patients.isEmpty()) return Optional.empty();
        if (patients.size()!=1) throw new RuntimeException("WHAT");
        return Optional.of(patients.get(0));
    }

    public List<NbcPatients> getPatientsWithSurnameLike(String surname) {
        String likeString = String.format("'%s%%'",surname.toUpperCase());
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        String sql = "SELECT * FROM NBC_PATIENTS " +
                "LEFT JOIN BAS_PEOPLE ON NBC_PATIENTS.BAS_PEOPLE_N = BAS_PEOPLE.N " +
                "WHERE UPPER(surname) LIKE " + likeString;
        List<NbcPatients> patients = namedParameterJdbcTemplate.query(sql, parameters, new RowNbcPatientsMapper());
        return patients;
    }

    public static class RowNbcPatientsMapper implements RowMapper<NbcPatients> {
        @Override
        public NbcPatients mapRow(ResultSet rs, int rowNum) throws SQLException {
            BasPeople basPeople = new BasPeopleDao.RowBasPeopleMapper().mapRow(rs, rowNum);
            basPeople.setN(rs.getLong("bas_people_n"));
            return NbcPatients.builder()
                    .basPeople(basPeople)
                    .n(rs.getLong(n.toString()))
                    .diagnosis(rs.getInt(diagnosis.toString()))
                    .case_history_num(rs.getInt(case_history_num.toString()))
                    .nbc_organizations_n(rs.getInt(nbc_organizations_n.toString()))
                    .build();
        }
    }
}
