package co.sfpy.factory;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectDBFactory {
	public static Connection getDBConnection() {
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			String user = "sfpy";
            String password = "sfpy";
            conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}
