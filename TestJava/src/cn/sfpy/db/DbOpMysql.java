package cn.sfpy.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mysql数据库查询优化实现类
 * 
 * @author zhangk
 * 
 */
public class DbOpMysql extends DbOpBase {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(DbOpMysql.class);

	public static void register() {
		IDbOp self = new DbOpMysql();
		// 注册自己
		DbOpMgr.register("com.mysql.jdbc.Driver", 0, self);
		DbOpMgr.register("com.mysql.jdbc.Driver", 5, self);
	}

	@Override
	protected List<?> search(Connection conn, String table, List<String> cols,
			String condition, List<Object> condValues, int start, int max,
			String orderBy, String groupBy, boolean bAsc, boolean bMap)
			throws Exception {
		if (conn == null || table == null) {
			String err = "调用search参数错误";
			log.error(err);
			throw new IllegalArgumentException(err);
		}

		// 1 构造语句
		StringBuffer sql = new StringBuffer(160);
		int i;

		if (cols == null) {
			sql.append("select * from ");
		} else {
			sql.append("select ");

			for (i = 0; i < cols.size(); i++) {
				if (i > 0)
					sql.append(",");
				sql.append(cols.get(i));
			}
			sql.append(" from ");
		}
		sql.append(table);

		if ((condition != null) && (condition.length() > 0)) {
			sql.append(" where " + condition);
		}

		if (groupBy != null) {
			sql.append(" group by " + groupBy);
		}

		if (orderBy != null) {
			sql.append(" order by " + orderBy);
			if (!bAsc)
				sql.append(" desc");
		}
		// 构造分页查询
		// 参见：google “MYSQL分页查询语句”
		sql.append(" LIMIT ").append(start);
		sql.append(", ").append(max);

		if (log.isDebugEnabled())
			log.debug("search SQL: " + sql);

		PreparedStatement pstmt = conn.prepareStatement(sql.toString());

		if (condValues != null) {
			preparedStatementSetValues(pstmt, condValues);
		}

		// 2 执行查询
		List<Object> ret = new ArrayList<Object>();
		try {
			ResultSet rs = pstmt.executeQuery();

			// 3 构造结果
			int colCount = 0;
			ResultSetMetaData rsmd = rs.getMetaData();
			colCount = rsmd.getColumnCount();

			int startCounter = start;
			int fetchCount = 0;
			if (bMap) {
				// map结果
				while (rs.next()) {
					if (fetchCount == max)
						break;

					if (startCounter >= start) {
						Map<String, Object> oneRec = new HashMap<String, Object>();
						for (i = 1; i <= colCount; i++) {
							oneRec.put(rsmd.getColumnName(i),
									getRsObj(i, rs, rsmd));
						}
						ret.add(oneRec);
						fetchCount++;
					} else {
						startCounter++;
					}
				}
			} else {
				// array结果
				while (rs.next()) {
					if (fetchCount == max)
						break;

					if (startCounter >= start) {
						Object[] oneRec = new Object[colCount];
						for (i = 0; i < colCount; i++) {
							oneRec[i] = getRsObj(i + 1, rs, rsmd);
						}
						ret.add(oneRec);
						fetchCount++;
					} else {
						startCounter++;
					}
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (pstmt != null)
				pstmt.close();
		}

		return ret;
	}
	
	//添加一个以数据库Label为key的方法,考虑修改会影响其他人所以另写在一个方法,希望在项目空窗期合并起来  by maj 2016-8-8
	@Override
	protected List<?> searchInLabel(Connection conn, String table, List<String> cols,
			String condition, List<Object> condValues, int start, int max,
			String orderBy, String groupBy, boolean bAsc, boolean bMap)
			throws Exception {
		if (conn == null || table == null) {
			String err = "调用search参数错误";
			log.error(err);
			throw new IllegalArgumentException(err);
		}

		// 1 构造语句
		StringBuffer sql = new StringBuffer(160);
		int i;

		if (cols == null) {
			sql.append("select * from ");
		} else {
			sql.append("select ");

			for (i = 0; i < cols.size(); i++) {
				if (i > 0)
					sql.append(",");
				sql.append(cols.get(i));
			}
			sql.append(" from ");
		}
		sql.append(table);

		if ((condition != null) && (condition.length() > 0)) {
			sql.append(" where " + condition);
		}

		if (groupBy != null) {
			sql.append(" group by " + groupBy);
		}

		if (orderBy != null) {
			sql.append(" order by " + orderBy);
			if (!bAsc)
				sql.append(" desc");
		}
		// 构造分页查询
		// 参见：google “MYSQL分页查询语句”
		sql.append(" LIMIT ").append(start);
		sql.append(", ").append(max);

		if (log.isDebugEnabled())
			log.debug("search SQL: " + sql);

		PreparedStatement pstmt = conn.prepareStatement(sql.toString());

		if (condValues != null) {
			preparedStatementSetValues(pstmt, condValues);
		}

		// 2 执行查询
		List<Object> ret = new ArrayList<Object>();
		try {
			ResultSet rs = pstmt.executeQuery();

			// 3 构造结果
			int colCount = 0;
			ResultSetMetaData rsmd = rs.getMetaData();
			colCount = rsmd.getColumnCount();

			int startCounter = start;
			int fetchCount = 0;
			if (bMap) {
				// map结果
				while (rs.next()) {
					if (fetchCount == max)
						break;

					if (startCounter >= start) {
						Map<String, Object> oneRec = new HashMap<String, Object>();
						for (i = 1; i <= colCount; i++) {
							oneRec.put(rsmd.getColumnLabel(i),
									getRsObj(i, rs, rsmd));
						}
						ret.add(oneRec);
						fetchCount++;
					} else {
						startCounter++;
					}
				}
			} else {
				// array结果
				while (rs.next()) {
					if (fetchCount == max)
						break;

					if (startCounter >= start) {
						Object[] oneRec = new Object[colCount];
						for (i = 0; i < colCount; i++) {
							oneRec[i] = getRsObj(i + 1, rs, rsmd);
						}
						ret.add(oneRec);
						fetchCount++;
					} else {
						startCounter++;
					}
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (pstmt != null)
				pstmt.close();
		}

		return ret;
	}
}
