/*
 * Created on 2006-11-6
 *
 * @author renwei
 * 
 * Copyright (C) 2006 KOAL SOFTWARE.
 */
package cn.sfpy.db;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 负责管理IDbOp的实现类，提供注册接口，数据库的优化类可注册后被使用。目前缺省只注册已知的优化类（见regDefault）。
 * 另外编写的优化类需要在getDbOp前注册后，才能被获取。
 * @author zhangk
 * @version 1.0
 * @since 1.0
 */
public class DbOpMgr {
	final static Logger log = LoggerFactory.getLogger(DbOpMgr.class);
	
	/**
	 * 注册用的map
	 */
	private static Map<String, IDbOp> m_regs = new HashMap<String, IDbOp>();
	
	/**
	 * 是否已经初始化，若没有调用缺省注册
	 */
	private static boolean m_init = false;
	
	private DbOpMgr() {
		// 静态类
	}
	
	/**
	 * 根据jdbcDriver和version构造一个字符串标识
	 * @param jdbcDriver
	 * @param version
	 * @return
	 */
	private static String getID(String jdbcDriver, int version) {
		return jdbcDriver + "(" + version + ")";
	}
	
	/**
	 * 注册IDbOp优化类
	 * @param jdbcDriver
	 * @param version
	 * @param dbOp
	 */
	public static void register(String jdbcDriver, int version, IDbOp dbOp) {
		m_regs.put(getID(jdbcDriver, version), dbOp);
	}
	
	/**
	 * 获取IDbOp实现。若不存在优化类，得到的就是IDbOpBase通用类
	 * @param jdbcDriver
	 * @param version
	 * @return
	 */
	public static IDbOp getDbOp(String jdbcDriver, int version) {
		if (!m_init) {
			regDefault();
			m_init = true;
		}
		IDbOp dbOp = (IDbOp)m_regs.get(getID(jdbcDriver, version));
		if (dbOp == null)
			dbOp = new DbOpBase();
		return dbOp;
	}
	
	/**
	 * 注册已知的优化类，注册发生在优化类被加载时
	 */
	private static void regDefault() {
		try {
		    // 注册数据库优化类,类中静态方法自动注册
		    DbOpOracle.register();
		    DbOpMysql.register();
		} catch (Exception e) {
			log.error("注册缺省数据库优化类失败", e);
		}
	}
	
}
