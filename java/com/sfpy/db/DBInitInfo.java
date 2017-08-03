package com.sfpy.db;

import java.util.ArrayList;
import java.util.List;

import com.sfpy.util.PropertiesUtils;

/**
 * 初始化，加载所有配置文件
 * 
 * @author SFPY
 */
public class DBInitInfo {
	public static List<DBbean> beans = new ArrayList<DBbean>();  

	static {
		DBbean dbBean = new DBbean();
		PropertiesUtils.loadFile("db.properties");
		String jdbcDriver = PropertiesUtils.getPropertyValue("driver");
		String dbUrl = PropertiesUtils.getPropertyValue("url");
		String dbUsername = PropertiesUtils.getPropertyValue("username");
		String dbPassword = PropertiesUtils.getPropertyValue("password");
		
		dbBean.setDriverName(jdbcDriver);
		dbBean.setUrl(dbUrl);;
		dbBean.setPassword(dbPassword);
		dbBean.setUserName(dbUsername);
		
		dbBean.setMinConnections(5);
		dbBean.setMaxConnections(100);
		
		dbBean.setPoolName(ConnectionPoolManager.poolName);
		beans.add(dbBean);
	}

}
