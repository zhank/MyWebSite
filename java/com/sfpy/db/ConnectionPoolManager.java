package com.sfpy.db;

import java.sql.Connection;
import java.util.Hashtable;

/**
 * 连接管理类
 * @author SFPY
 */
public class ConnectionPoolManager {
	
	public static String poolName = "dbPool";
	
	//连接池存放
	public Hashtable<String, IConnectionPool> pools = new Hashtable<String, IConnectionPool>();
	
	public static ConnectionPoolManager getInstance() {
		return Singtonle.instance;
	}
	
	public static class Singtonle {
		private static ConnectionPoolManager instance = new ConnectionPoolManager();
	}
	
	//初始化
	private ConnectionPoolManager() {
		init();
	}
	
	 // 初始化所有的连接池  
	public void init() {
		for(int i =0;i<DBInitInfo.beans.size();i++){  
            DBbean bean = DBInitInfo.beans.get(i);  
            ConnectionPool pool = new ConnectionPool(bean);  
            if(pool != null){  
                pools.put(bean.getPoolName(), pool);  
                System.out.println("Info:Init connection successed ->" +bean.getPoolName());  
            }  
        }  
	}
	
	/**
	 * 清空连接池
	 */
	public void destroy(String poolName) {
		IConnectionPool pool = getPool(poolName);
		if(pool != null) {
			pool.destroy();
		}
	}
	
	/**
	 * 获得连接,根据连接池名字 获得连接  
	 * @return
	 */
	public Connection getConnection(String poolName) {
		Connection conn = null;
		if(pools.size() > 0 && pools.containsKey(poolName)) {
			conn = getPool(poolName).getConnection();
		} else {
			  System.out.println("Error:Can't find this connecion pool ->"+poolName);  
		}
		  return conn;  
	}
	
	/**
	 * 获得连接池  
	 * @param poolName
	 * @return
	 */
    public IConnectionPool getPool(String poolName){  
        IConnectionPool pool = null;  
        if(pools.size() > 0){  
             pool = pools.get(poolName);  
        }  
        return pool;  
    }  
}
