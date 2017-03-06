package lgk.nsbc.template.dao;

import lgk.nsbc.template.model.NbcPatients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class NbcProcDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Long countProceduresForPatient(NbcPatients nbcPatients) {
        MapSqlParameterSource parameters = new MapSqlParameterSource("id", nbcPatients.getN());
        String sql = "SELECT COUNT (*) FROM NBC_PROC WHERE NBC_PATIENTS_N = :id";
        return namedParameterJdbcTemplate.queryForObject(sql, parameters, Long.class);
    }
}
