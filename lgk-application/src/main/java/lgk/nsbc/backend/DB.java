package lgk.nsbc.backend;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс для получение соединеня с базой данных, при помощи JDBC
 * Текущая база данных FireBird, версия 2.5.5
 */
public class DB {
	static {
		try {
			DriverManager.registerDriver(new org.firebirdsql.jdbc.FBDriver());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	static final String url = "jdbc:firebirdsql://localhost:3050/lgknew?encoding=WIN1251";
	static final String user_name = "SYSDBA";
	static final String user_password = "masterkey";

	public static Connection getConnection() throws SQLException {
		try {
			return DriverManager.getConnection(url, user_name, user_password);
		} catch (SQLException e) {
			Logger.getGlobal().log(Level.SEVERE,"",e);
			throw e;
		}
	}

	public static DSLContext getDSLContext() throws SQLException {
		Connection con = DB.getConnection();
		return DSL.using(con, SQLDialect.FIREBIRD_2_5);
	}
}
