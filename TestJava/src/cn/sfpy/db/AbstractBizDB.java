/**
 * @(#)AbstractBizDB.java
 * 
 * @author renw,xujun
 * @version 1.0 2006-8-1
 * 
 * Copyright (C) 2000,2006 , KOAL, Inc.
 */
package cn.sfpy.db;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Purpose:实现抽象的IDbCall，封装一系列IDbCall的公用方法，对需要对数据库进行处理的类，可以继承此类进行实现
 * 建议每个实例都采用单例模式进行初始化和进行管理
 * 
 * @see
 * @since 1.1.0
 */
public abstract class AbstractBizDB implements IDbCall, Serializable {
	private static final long serialVersionUID = 1L;

	protected static Logger log = LoggerFactory.getLogger(IDbCall.class);

	// 因为各个子类需要指定不同名的连接池，因此应该在子类中指定，防止重复使用同名连接池出现错误
	// protected String DB_POOL = "BIZ_POOL";

	/**
	 * 内部实际调用的IDbOp实现类，缺省是无优化的通用实现
	 */
	protected IDbOp m_dbOp = new DbOpBase();

	/**
	 * 根据驱动实现优化的IDbOp实现类
	 * 
	 * @param jdbcDriver
	 * @param version
	 */
	public void enableOptimize(String jdbcDriver, int version) {
		IDbOp newDbOp = DbOpMgr.getDbOp(jdbcDriver, version);
		if (newDbOp != null) {
			m_dbOp = newDbOp;
		}
	}

	/**
	 * 获得池的名称
	 * 
	 * @return
	 */
	abstract public String getPoolName();

	/**
	 * 设置池的名称
	 * 
	 * @return
	 */
	abstract public void setPoolName(String poolName);

	/**
	 * 初始化数据库
	 * 
	 * @throws Exception
	 */
	public abstract void initDB() throws Exception;

	/**
	 * 获取FieldList的字段名列表
	 * 
	 * @param fields
	 * @return
	 */
	public static List<String> getListFromFields(Field[] fields) {
		if (fields == null) {
			return null;
		}
		List<String> fieldList = new ArrayList<String>(fields.length);
		for (int i = 0; i < fields.length; i++) {
			fieldList.add(fields[i].name);
		}
		return fieldList;
	}

	/**
	 * 基本数据操作--插入
	 * 
	 * @param table
	 * @param dataMap
	 * @throws Exception
	 */
	public int insert(String table, Map<String, Object> dataMap)
			throws Exception {
		Connection conn = getConn(false);
		try {
			return m_dbOp.insert(conn, table, dataMap);
		} finally {
			closeConn(conn);
		}
	}

	/**
	 * 基本数据操作--插入
	 * 
	 * @param table
	 * @param dataMap
	 * @param isAutoGenKey
	 *            是否返回自增长列, true返回
	 * @throws Exception
	 */
	public int insert(String table, Map<String, Object> dataMap,
			boolean isAutoGenKey) throws Exception {
		Connection conn = getConn(false);
		try {
			return m_dbOp.insert(conn, table, dataMap, isAutoGenKey);
		} finally {
			closeConn(conn);
		}
	}

	
	public int insert(String table, Map<String, Object> dataMap,
			String[] colList, boolean isAutoGenKey) throws Exception {
		Connection conn = getConn(false);
		try {
			return m_dbOp.insert(conn, table, dataMap, colList, isAutoGenKey);
		} finally {
			closeConn(conn);
		}
	}
	
	/**
	 * 基本数据操作--插入（插入前判断）
	 * 
	 * @param table
	 * @param dataMap
	 * @param cond
	 * @param condValues
	 * @throws Exception
	 */
	public int insertIfNotExists(String table, Map<String, Object> dataMap, String cond, List<Object> condValues)
			throws Exception {
		Connection conn = getConn(false);
		try {
			return m_dbOp.insertIfNotExists(conn, table, dataMap, cond, condValues);
		} finally {
			closeConn(conn);
		}
	}

	/**
	 * 更新表记录的多个字段
	 * 
	 * @param table
	 *            表名
	 * @param dataMap
	 *            待更新数据的Map，其中的key就是字段名，value是对应该字段的数据
	 * @param condition
	 *            条件语句
	 * @param condValues
	 *            条件参数列表
	 * @throws Exception
	 */
	public void update(String table, Map<String, Object> dataMap,
			String condition, List<Object> condValues) throws Exception {
		Connection conn = getConn(false);

		try {
			m_dbOp.update(conn, table, dataMap, condition, condValues);
		} finally {
			closeConn(conn);
		}
	}

	/**
	 * 基本数据操作--更新
	 * 
	 * @param table
	 * @param name
	 * @param value
	 * @param condition
	 * @throws Exception
	 */
	public void update(String table, String name, Object value, String condition)
			throws Exception {
		Connection conn = getConn(false);

		try {
			m_dbOp.update(conn, table, name, value, condition);
		} finally {
			closeConn(conn);
		}
	}

	/**
	 * 基本数据操作--更新
	 * 
	 * @param table
	 * @param dataMap
	 * @param condition
	 * @throws Exception
	 */
	public void update(String table, Map<String, Object> dataMap,
			String condition) throws Exception {
		Connection conn = getConn(false);

		try {
			m_dbOp.update(conn, table, dataMap, condition, null);
		} finally {
			closeConn(conn);
		}
	}

	/**
	 * 基本数据操作--删除
	 * 
	 * @param table
	 * @param condition
	 * @throws Exception
	 */
	public void delete(String table, String condition) throws Exception {
		Connection conn = getConn(false);
		try {
			m_dbOp.delete(conn, table, condition, null);
		} finally {
			closeConn(conn);
		}
	}

	/**
	 * 删除记录
	 * 
	 * @param table
	 *            表名
	 * @param condition
	 *            条件语句
	 * @param condValues
	 *            条件参数列表
	 * @throws Exception
	 */
	public void delete(String table, String preparedCond,
			List<Object> condValues) throws Exception {
		Connection conn = getConn(false);
		try {
			m_dbOp.delete(conn, table, preparedCond, condValues);
		} finally {
			closeConn(conn);
		}
	}

	/**
	 * 基本数据操作--查询
	 * 
	 * @param table
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchAsMapList(String table)
			throws Exception {
		return searchAsMapList(table, null, null, null, 0, IDbOp.DEFAULT_MAX,
				null, false);
	}

	/**
	 * 基本数据操作--查询
	 * 
	 * @param table
	 * @param fields
	 * @return Map类型
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchAsMapList(String table,
			Field[] fields) throws Exception {
		return searchAsMapList(table, fields, null, null, 0, IDbOp.DEFAULT_MAX,
				null, false);
	}

	/**
	 * 基本数据操作--查询
	 * 
	 * @param table
	 * @param fields
	 * @param condition
	 * @return Map类型
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchAsMapList(String table,
			Field[] fields, String condition) throws Exception {
		return searchAsMapList(table, fields, condition, null, 0,
				IDbOp.DEFAULT_MAX, null, false);
	}
	
	public List<Map<String, Object>> searchAsMapList(String table,
				Field[] fields, String condition, List<Object> condValues) throws Exception {
		return searchAsMapList(table, fields, condition, condValues, 0, 
				IDbOp.DEFAULT_MAX, null, false);
	}

	/**
	 * 基本数据操作--查询(返回所有字段)
	 * 
	 * @param table
	 * @param condition
	 * @return Map类型
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchAsMapList(String table,
			String condition) throws Exception {
		return searchAsMapList(table, null, condition, null, 0,
				IDbOp.DEFAULT_MAX, null, false);
	}

	/**
	 * 基本数据操作--查询(返回所有字段)
	 * 
	 * @param table
	 * @param condition
	 * @param condValues
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchAsMapList(String table,
			String condition, List<Object> condValues) throws Exception {
		return searchAsMapList(table, null, condition, condValues, 0,
				IDbOp.DEFAULT_MAX, null, false);
	}

	/**
	 * 基本数据操作--查询
	 * 
	 * @param table
	 * @param fields
	 * @param condition
	 * @param condValues
	 * @param start
	 * @param max
	 * @param orderBy
	 * @param bAsc
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchAsMapList(String table,
			Field[] fields, String condition, List<Object> condValues,
			int start, int max, String orderBy, boolean bAsc) throws Exception {
		Connection conn = getConn(true);
		try {
			return m_dbOp.searchAsMapList(conn, table,
					getListFromFields(fields), condition, condValues, start,
					max, orderBy, null, bAsc);
		} finally {
			closeConn(conn);
		}
	}

	public List<Map<String, Object>> searchAsMapList(String table,
			Field[] fields, String condition, List<Object> condValues,
			int start, int max, String orderBy, String groupBy, boolean bAsc)
			throws Exception {
		return searchAsMapList(table, AbstractBizDB.getListFromFields(fields), 
				condition, condValues, start, max, orderBy, groupBy, bAsc);
	}
	
	/**
	 * 通用查询，以List[map1, map2...]形式返回
	 * 
	 * @param table
	 *            表名
	 * @param cols
	 *            需要返回的字段名列表
	 * @param condition
	 *            条件语句
	 * @param condValues
	 *            条件参数列表
	 * @param start
	 *            记录的开始位置
	 * @param max
	 *            返回最大记录个数
	 * @param orderBy
	 *            排序字段名
	 * @param groupBy
	 *            分组字段名
	 * @param bAsc
	 *            是否升序
	 * @return 以List[map1, map2...]形式返回，cols指定的字段数据，最大为max个
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchAsMapList(String table,
			List<String> cols, String condition, List<Object> condValues,
			int start, int max, String orderBy, String groupBy, boolean bAsc)
					throws Exception {
		Connection conn = getConn(true);

		try {
			return m_dbOp.searchAsMapList(conn, table, cols, condition,
					condValues, start, max, orderBy, groupBy, bAsc);
		} finally {
			closeConn(conn);
		}
	}

	//添加一个以数据库Label为key的方法,考虑修改会影响其他人所以另写在一个方法,希望在项目空窗期合并起来  by maj 2016-8-8
	public List<Map<String, Object>> searchAsMapListInLabel(String table,
			List<String> cols, String condition, List<Object> condValues,
			int start, int max, String orderBy, String groupBy, boolean bAsc)
					throws Exception {
		Connection conn = getConn(true);

		try {
			return m_dbOp.searchAsMapListInLabel(conn, table, cols, condition, condValues, start, max, orderBy, groupBy, bAsc);
		} finally {
			closeConn(conn);
		}
	}
	/**
	 * 基本数据操作--查询
	 * 
	 * @param table
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> searchAsArrayList(String table) throws Exception {
		Connection conn = getConn(true);

		try {
			return m_dbOp.searchAsArrayList(conn, table, null, null, null, 0,
					IDbOp.DEFAULT_MAX, null, null, false);
		} finally {
			closeConn(conn);
		}
	}

	/**
	 * 基本数据操作--查询
	 * 
	 * @param table
	 * @param fields
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> searchAsArrayList(String table, Field[] fields)
			throws Exception {
		Connection conn = getConn(true);

		try {
			return m_dbOp.searchAsArrayList(conn, table,
					getListFromFields(fields), null, null, 0,
					IDbOp.DEFAULT_MAX, null, null, false);
		} finally {
			closeConn(conn);
		}
	}

	/**
	 * 基本数据操作--查询
	 * 
	 * @param table
	 * @param fields
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> searchAsArrayList(String table, List<String> cols)
			throws Exception {
		Connection conn = getConn(true);

		try {
			return m_dbOp.searchAsArrayList(conn, table, cols, null, null, 0,
					IDbOp.DEFAULT_MAX, null, null, false);
		} finally {
			closeConn(conn);
		}
	}

	/**
	 * 基本数据操作--查询
	 * 
	 * @param table
	 * @param fields
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> searchAsArrayList(String table, Field[] fields,
			String condition) throws Exception {
		Connection conn = getConn(true);

		try {
			return m_dbOp.searchAsArrayList(conn, table,
					getListFromFields(fields), condition, null, 0,
					IDbOp.DEFAULT_MAX, null, null, false);
		} finally {
			closeConn(conn);
		}
	}

	/**
	 * 基本数据操作--查询
	 * 
	 * @param table
	 * @param fields
	 * @param condition
	 * @param orderBy
	 * @param bAsc
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> searchAsArrayList(String table, Field[] fields,
			String condition, String orderBy, boolean bAsc) throws Exception {
		Connection conn = getConn(true);

		try {
			return m_dbOp.searchAsArrayList(conn, table,
					getListFromFields(fields), condition, null, 0,
					IDbOp.DEFAULT_MAX, orderBy, null, bAsc);
		} finally {
			closeConn(conn);
		}
	}

	/**
	 * 基本数据操作--查询
	 * 
	 * @param table
	 * @param fields
	 * @param condition
	 * @param condValues
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> searchAsArrayList(String table, Field[] fields,
			String condition, List<Object> condValues) throws Exception {
		Connection conn = getConn(true);

		try {
			return m_dbOp.searchAsArrayList(conn, table,
					getListFromFields(fields), condition, condValues, 0,
					IDbOp.DEFAULT_MAX, null, null, false);
		} finally {
			closeConn(conn);
		}
	}

	/**
	 * 基本数据操作--查询
	 * 
	 * @param table
	 * @param fields
	 * @param condition
	 * @param condValues
	 * @param start
	 * @param max
	 * @param orderBy
	 * @param bAsc
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> searchAsArrayList(String table, Field[] fields,
			String condition, List<Object> condValues, int start, int max,
			String orderBy, boolean bAsc) throws Exception {
		Connection conn = getConn(true);
		List<Object[]> result = null;
		try {

			result = m_dbOp.searchAsArrayList(conn, table,
					getListFromFields(fields), condition, condValues, start,
					max, orderBy, null, bAsc);
		} finally {
			closeConn(conn);
		}
		return result;
	}

	/**
	 * 基本数据操作--查询
	 * 
	 * @param table
	 * @param fields
	 * @param condition
	 * @param condValues
	 * @param start
	 * @param max
	 * @param orderBy
	 * @param groupBy
	 * @param bAsc
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> searchAsArrayList(String table, Field[] fields,
			String condition, List<Object> condValues, int start, int max,
			String orderBy, String groupBy, boolean bAsc) throws Exception {
		Connection conn = getConn(true);

		try {
			return m_dbOp.searchAsArrayList(conn, table,
					getListFromFields(fields), condition, condValues, start,
					max, orderBy, groupBy, bAsc);
		} finally {
			closeConn(conn);
		}
	}

	/**
	 * 基本数据操作--查询
	 * 
	 * @param table
	 * @param fields
	 * @param condition
	 * @param condValues
	 * @param start
	 * @param max
	 * @param orderBy
	 * @param groupBy
	 * @param bAsc
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> searchAsArrayList(String table, List<String> fields,
			String condition, List<Object> condValues, int start, int max,
			String orderBy, String groupBy, boolean bAsc) throws Exception {
		Connection conn = getConn(true);

		try {
			return m_dbOp.searchAsArrayList(conn, table, fields, condition,
					condValues, start, max, orderBy, groupBy, bAsc);
		} finally {
			closeConn(conn);
		}
	}

	/**
	 * 基本数据操作--查询一条数据
	 * 
	 * @param table
	 * @param fields
	 * @param condition
	 * @param condValues
	 * @return
	 * @throws Exception
	 */
	public Object[] getOneRowAsArray(String table, Field[] fields,
			String condition) throws Exception {
		return getOneRowAsArray(table, fields, condition, null);
	}

	/**
	 * 基本数据操作--查询一条数据
	 * 
	 * @param table
	 * @param fields
	 * @param condition
	 * @param condValues
	 * @return
	 * @throws Exception
	 */
	public Object[] getOneRowAsArray(String table, Field[] fields,
			String condition, List<Object> condValues) throws Exception {
		Connection conn = getConn(true);

		try {
			return m_dbOp.getOneRowAsArray(conn, table,
					getListFromFields(fields), condition, condValues, true);
		} finally {
			closeConn(conn);
		}
	}

	/**
	 * 基本数据操作--查询一条数据
	 * 
	 * @param table
	 * @param cols
	 * @param condition
	 * @param condValues
	 * @return
	 * @throws Exception
	 */
	public Object[] getOneRowAsArray(String table, List<String> cols,
			String condition, List<Object> condValues) throws Exception {
		Connection conn = getConn(true);

		try {
			return m_dbOp.getOneRowAsArray(conn, table, cols, condition,
					condValues, true);
		} finally {
			closeConn(conn);
		}
	}

	/**
	 * 获取一条记录，以数组方式返回。
	 * 
	 * @param table
	 *            表名
	 * @param cols
	 *            需要返回的字段名列表
	 * @param condition
	 *            条件语句
	 * @param condValues
	 *            条件参数列表
	 * @param mustUnique
	 *            是否必须唯一？若为True，但实际又有多条记录，将抛出异常
	 * @return
	 * @throws Exception
	 */
	public Object[] getOneRowAsArray(String table, List<String> cols,
			String condition, List<Object> condValue, boolean mustUnique)
			throws Exception {
		Connection conn = getConn(true);

		try {
			return m_dbOp.getOneRowAsArray(conn, table, cols, condition,
					condValue, true);
		} finally {
			closeConn(conn);
		}
	}

	/**
	 * 基本数据操作--查询一条数据
	 * 
	 * @param table
	 * @param cols
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getOneRowAsMap(String table, List<String> cols,
			String condition) throws Exception {
		Connection conn = getConn(true);
		try {
			return m_dbOp.getOneRowAsMap(conn, table, cols, condition, null,
					true);
		} finally {
			closeConn(conn);
		}
	}
	
	/**
	 * 基本数据操作--查询一条数据
	 * 
	 * @param table
	 * @param cols
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getOneRowAsMap(String table, Field[] fields,
			String condition) throws Exception {
		Connection conn = getConn(true);
		try {
			return m_dbOp.getOneRowAsMap(conn, table,
					getListFromFields(fields), condition, null, true);
		} finally {
			closeConn(conn);
		}
	}

	/**
	 * 获取一条记录，以map返回
	 * 
	 * @param table
	 *            表名
	 * @param cols
	 *            需要返回的字段名列表
	 * @param condition
	 *            条件语句
	 * @param condValues
	 *            条件参数列表
	 * @param mustUnique
	 *            是否必须唯一？若为True，但实际又有多条记录，将抛出异常
	 * @return 数据map
	 * @throws Exception
	 */
	public Map<String, Object> getOneRowAsMap(String table, List<String> cols,
			String condition, List<Object> condValues, boolean mustUnique)
			throws Exception {
		Connection conn = getConn(true);

		try {
			return m_dbOp.getOneRowAsMap(conn, table, cols, condition,
					condValues, mustUnique);
		} finally {
			closeConn(conn);
		}
	}

	/**
	 * 计算记录数
	 * 
	 * @param table
	 * @return
	 * @throws Exception
	 */
	public int getCount(String table) throws Exception {
		return getCount(table, null, null, null);
	}

	/**
	 * 计算记录数
	 * 
	 * @param table
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public int getCount(String table, String condition) throws Exception {
		return getCount(table, condition, null, null);
	}

	/**
	 * 计算记录数
	 * 
	 * @param table
	 * @param condition
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public int getCount(String table, String condition, Object value)
			throws Exception {

		return getCount(table, condition,
				Arrays.asList(new Object[] { value }), null);
	}

	/**
	 * 计算记录数
	 * 
	 * @param table
	 * @param condition
	 * @param condValues
	 * @return
	 * @throws Exception
	 */
	public int getCount(String table, String condition, List<Object> condValues)
			throws Exception {
		return getCount(table, condition, condValues, null);
	}

	/**
	 * 计算记录数
	 * 
	 * @param table
	 * @param condition
	 * @param condValues
	 * @param distinct
	 * @return
	 * @throws Exception
	 */
	public int getCount(String table, String condition,
			List<Object> condValues, String distinct) throws Exception {
		Connection conn = getConn(true);
		try {
			return m_dbOp
					.getCount(conn, table, condition, condValues, distinct);
		} finally {
			closeConn(conn);
		}
	}

	public Integer getMaxInt(String table, String col) throws Exception {
		return getMaxInt(table, col, null);
	}

	public Integer getMaxInt(String table, String col, String condition)
			throws Exception {
		return getMaxInt(table, col, condition, null);
	}

	public Integer getMaxInt(String table, String col, String condition,
			List<Object> condValues) throws Exception {
		Connection conn = getConn(true);
		try {
			return m_dbOp.getMax(Integer.class, conn, table, col, condition,
					condValues);
		} finally {
			closeConn(conn);
		}
	}
	
	public Integer getMinInt(String table, String col) throws Exception {
		return getMaxInt(table, col, null);
	}

	public Integer getMinInt(String table, String col, String condition)
			throws Exception {
		return getMaxInt(table, col, condition, null);
	}

	public Integer getMinInt(String table, String col, String condition,
			List<Object> condValues) throws Exception {
		Connection conn = getConn(true);
		try {
			return m_dbOp.getMin(Integer.class, conn, table, col, condition,
					condValues);
		} finally {
			closeConn(conn);
		}
	}

	/**
	 * 根据ID查到某个单一字段(如：根据部门ID查到部门名称)
	 * 
	 * @author huangc (2006-07-24)
	 * @param strTable
	 *            - 要查询的表名称(如："TB_DEPART")
	 * @param fieldID
	 *            - 要查询的ID字段(如：TbDepart.DEPART_ID)
	 * @param fieldObject
	 *            - 要返回字段(如：TbDepart.DEPART_NAME)
	 * @param param
	 *            - 要查询的ID值(如：map.get(TbDepart.DEPART_NAME.name))
	 * @return 跟ID值匹配的内容
	 */
	public Object getObjectFromID(String strTable, Field fieldID,
			Field fieldObject, Object param) throws Exception {
		return getObjectFromID(strTable, fieldID, fieldObject, param, null);
	}

	/**
	 * 根据ID查到某个单一字段(如：根据部门ID查到部门名称)
	 * 
	 * @author huangc (2006-07-24)
	 * @param strTable
	 *            - 要查询的表名称(如："TB_DEPART")
	 * @param fieldID
	 *            - 要查询的ID字段(如：TbDepart.DEPART_ID)
	 * @param fieldObject
	 *            - 要返回字段(如：TbDepart.DEPART_NAME)
	 * @param param
	 *            - 要查询的ID值(如：map.get(TbDepart.DEPART_NAME.name))
	 * @param otherCond
	 *            - 其他条件(如：TbDepart.DEPART_PARENT.name + " IS NOT NULL")
	 * @return 跟ID值匹配的内容
	 */
	public Object getObjectFromID(String strTable, Field fieldID,
			Field fieldObject, Object param, String otherCond) throws Exception {
		Object[] oneRec;
		Field[] m_fields = { fieldObject, };
		String cond = fieldID.toSqlEQ(param);
		if (otherCond != null) {
			cond += " AND " + otherCond;
		}
		List<?> ob = searchAsArrayList(strTable, m_fields, cond);
		if (ob.size() == 0) {
			return null;
		} else {
			oneRec = (Object[]) ob.get(0);
			return oneRec[0];
		}
	}

	/**
	 * 获取数据库连接（当此实例尚未初始化时先进行初始化)
	 * 
	 * @param bAutoCommit
	 *            是否自动提交
	 * @return
	 * @throws Exception
	 */
	public Connection getConn(boolean bAutoCommit) throws Exception {
		Connection conn = null;
		try {
			conn = DbcpManager.getConnection(getPoolName());
			if (log.isDebugEnabled()) {
				log.debug("Get DB(" + this.getPoolName() + ") connection: "
						+ conn);
			}
			conn.setAutoCommit(bAutoCommit);
		} catch (Exception e) {
			String err = "创建数据库(" + getPoolName() + ")连接失败，请检查数据库或网络连接是否正常。异常："
					+ e.getMessage();
			log.error(err, e);
			throw new Exception(err, e);
		}
		return conn;
	}

	/**
	 * 释放数据库连接
	 * 
	 * @param conn
	 * @throws Exception
	 */
	public void closeConn(Connection conn) throws Exception {
		if (conn == null)
			return;
		try {
			if (!conn.getAutoCommit())
				conn.commit();
		} catch (Exception e) {
			log.error("提交commit异常(" + getPoolName() + ")", e);
		} finally {
			try {
				// conn.setAutoCommit(true);
				conn.close();
			} catch (Exception e) {
				log.error("关闭conn：", e);
			}
		}
	}

	/**
	 * 判断一个值是否已存在数据库中 true:存在；flase:不存在
	 * find_value要查讯的值，field要查询字段的信息，tableName表名，ifString查询的字段是否为字符
	 */
	public boolean getValue(final String find_value, final Field field,
			final String tableName, final boolean ifString) {
		return getValue(find_value, field, tableName, ifString, "");
	}

	/**
	 * otherCon其他条件
	 */
	public boolean getValue(final String find_value, final Field field,
			final String tableName, final boolean ifString,
			final String otherCond) {

		Field[] fields = { field };
		StringBuffer condition = new StringBuffer(field.name);
		if (ifString) {
			condition.append("='");
			condition.append(find_value);
			condition.append("'");
		} else {
			condition.append("=");
			condition.append(find_value);
		}
		if ((otherCond != null) && (!otherCond.equals(""))) {
			condition.append(" and ");
			condition.append(otherCond);
		}

		try {
			List<?> ob = searchAsArrayList(tableName, fields,
					condition.toString());
			if (ob.size() >= 1) {
				return true;
			}
		} catch (Exception e) {
			// System.out.println("erro:" + e);
		}
		return false;
	}

	/**
	 * 批量执行sql语句
	 * 
	 * @param table
	 * @param condition
	 * @throws Exception
	 */
	public void execSQL(String[] sqls) throws Exception {
		Connection conn = getConn(false);
		try {
			m_dbOp.execSQL(conn, sqls);
		} finally {
			closeConn(conn);
		}
	}
	
}