package lgk.nsbc.backend;

import lgk.nsbc.backend.entity.Session;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Управление сессией в базе
 */
public class SessionManager {

	final private QueryRunner qr = new QueryRunner();
	final private BeanHandler<Session> handler = new BeanHandler<>(Session.class);
	private final ScalarHandler<Long> rs = new ScalarHandler<>();
	private static final String sessionTableName = "sys_sessions";
	public  static final SessionManager instance = new SessionManager();

	public Session loadSession(String lgkSessId) {
		String sql =
			"SELECT\n" +
				"sys_agents.n,\n" +
				"sys_agents.name AS login,\n" +
				"sys_agents.nbc_org_n_deafult AS DeafultOrgN,\n" +
				"bas_people.name,\n" +
				"bas_people.surname\n" +
				"FROM sys_sessions\n" +
				"JOIN sys_agents ON sys_agents.n = sys_sessions.agent_n\n" +
				"JOIN bas_people ON bas_people.n = sys_agents.people_n\n" +
				"WHERE sid = ?\n";
		try (
			Connection con = DB.getConnection()
		) {
			return qr.query(con, sql, handler, lgkSessId);
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Получить N сессии из таблицы sys_sessions
	 * @param con,lgkSessId
	 * @return N (Primary key)
	 **/
	public Long IdSession(Connection con
		,String lgkSessId
	) throws SQLException {
		String sql =
			"SELECT n\n" +
		    "FROM " +sessionTableName + "\n"+
		    "WHERE sid = ?\n";
		return qr.query(con, sql, rs, lgkSessId);
	}
}
