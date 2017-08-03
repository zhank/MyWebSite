package com.sfpy.db;

import java.sql.Connection;

public class ConnectDBFactory {
	public static Connection getDBConnection() {
		Connection conn = ConnectionPoolManager.getInstance().getConnection("dbPool");
		return conn;
	}
}
