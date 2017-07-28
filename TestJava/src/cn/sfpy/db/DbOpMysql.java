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
 * Mysql���ݿ��ѯ�Ż�ʵ����
 * 
 * @author zhangk
 * 
 */
public class DbOpMysql extends DbOpBase {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(DbOpMysql.class);

	public static void register() {
		IDbOp self = new DbOpMysql();
		// ע���Լ�
		DbOpMgr.register("com.mysql.jdbc.Driver", 0, self);
		DbOpMgr.register("com.mysql.jdbc.Driver", 5, self);
	}

	@Override
	protected List<?> search(Connection conn, String table, List<String> cols,
			String condition, List<Object> condValues, int start, int max,
			String orderBy, String groupBy, boolean bAsc, boolean bMap)
			throws Exception {
		if (conn == null || table == null) {
			String err = "����search��������";
			log.error(err);
			throw new IllegalArgumentException(err);
		}

		// 1 �������
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
		// �����ҳ��ѯ
		// �μ���google ��MYSQL��ҳ��ѯ��䡱
		sql.append(" LIMIT ").append(start);
		sql.append(", ").append(max);

		if (log.isDebugEnabled())
			log.debug("search SQL: " + sql);

		PreparedStatement pstmt = conn.prepareStatement(sql.toString());

		if (condValues != null) {
			preparedStatementSetValues(pstmt, condValues);
		}

		// 2 ִ�в�ѯ
		List<Object> ret = new ArrayList<Object>();
		try {
			ResultSet rs = pstmt.executeQuery();

			// 3 ������
			int colCount = 0;
			ResultSetMetaData rsmd = rs.getMetaData();
			colCount = rsmd.getColumnCount();

			int startCounter = start;
			int fetchCount = 0;
			if (bMap) {
				// map���
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
				// array���
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
	
	//���һ�������ݿ�LabelΪkey�ķ���,�����޸Ļ�Ӱ��������������д��һ������,ϣ������Ŀ�մ��ںϲ�����  by maj 2016-8-8
	@Override
	protected List<?> searchInLabel(Connection conn, String table, List<String> cols,
			String condition, List<Object> condValues, int start, int max,
			String orderBy, String groupBy, boolean bAsc, boolean bMap)
			throws Exception {
		if (conn == null || table == null) {
			String err = "����search��������";
			log.error(err);
			throw new IllegalArgumentException(err);
		}

		// 1 �������
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
		// �����ҳ��ѯ
		// �μ���google ��MYSQL��ҳ��ѯ��䡱
		sql.append(" LIMIT ").append(start);
		sql.append(", ").append(max);

		if (log.isDebugEnabled())
			log.debug("search SQL: " + sql);

		PreparedStatement pstmt = conn.prepareStatement(sql.toString());

		if (condValues != null) {
			preparedStatementSetValues(pstmt, condValues);
		}

		// 2 ִ�в�ѯ
		List<Object> ret = new ArrayList<Object>();
		try {
			ResultSet rs = pstmt.executeQuery();

			// 3 ������
			int colCount = 0;
			ResultSetMetaData rsmd = rs.getMetaData();
			colCount = rsmd.getColumnCount();

			int startCounter = start;
			int fetchCount = 0;
			if (bMap) {
				// map���
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
				// array���
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
