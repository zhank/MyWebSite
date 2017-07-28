package cn.sfpy.session;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import co.sfpy.factory.ConnectDBFactory;

public class JaveSession {
	 public static String getSaveObjectSql(Object object) throws Exception {
		 // ����һ��sql�ַ���
	        String sql = "insert into ";
	        // �õ��������
	        Class c = object.getClass();
	        // �õ����������еķ���
	        Method[] methods = c.getMethods();
	        
	        // �õ����������е�����
	        Field[] fields = c.getFields();
	        
	        // �õ������������
	        String cName = c.getName();
	        
	        // ����������н���������
	        String tableName = cName.substring(cName.lastIndexOf(".") + 1,
	                cName.length());
	        
	        sql += tableName + "(";
	        
	        List<String> mList = new ArrayList<String>();
	        List<Object> vList = new ArrayList<Object>();
	      /*  Method mainMethod = c.getMethod("getMainId");
	        Object classObj = c.newInstance();*/
	        
	        for (Method method : methods) {
	        	  String mName = method.getName();
	        	  if (mName.startsWith("get") && !mName.startsWith("getClass")) {
	        		  String fieldName = mName.substring(3, mName.length());
	        		  mList.add(fieldName);
	        		  System.out.println("�ֶ�����----->" + fieldName);
	        		    try {
	                        Object value = method.invoke(object, null);
	                        System.out.println("ִ�з������ص�ֵ��" + value);
	                        if (value instanceof String) {
	                            vList.add("\'" + value + "\'");
	                            System.out.println("�ֶ�ֵ------>" + value);
	                        } else {
	                            vList.add(value);
	                        }
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                    }
	        	  }
	        }
	        for (int i = 0; i < mList.size(); i++) {
	            if (i < mList.size() - 1) {
	                sql += mList.get(i) + ",";
	            } else {
	                sql += mList.get(i) + ") values(";
	            }
	        }
	        for (int i = 0; i < vList.size(); i++) {
	            if (i < vList.size() - 1) {
	                sql += vList.get(i) + ",";
	            } else {
	                sql += vList.get(i) + ")";
	            }
	        }
	        return sql;
	 }
	 
	 
	 /**
	     * �����󱣴浽���ݿ���
	     *
	     * @param object
	     *            ����Ҫ����Ķ���
	     * @return������ִ�еĽ��;1:��ʾ�ɹ���0:��ʾʧ��
	 * @throws Exception 
	     */
	    public int saveObject(Object object) throws Exception {
	        Connection con = ConnectDBFactory.getDBConnection();
	        String sql = getSaveObjectSql(object);
	        try {
	            // Statement statement=(Statement) con.createStatement();
	            PreparedStatement psmt = con.prepareStatement(sql);
	            psmt.executeUpdate();
	            return 1;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return 0;
	        }
	    }
	    
	    /**
	     * �����ݿ���ȡ�ö���
	     *
	     * @param arg0
	     *            ��������������
	     * @param id
	     *            �������id
	     * @return:��Ҫ���ҵĶ���
	     */
	    public Object getObject(String className, String mainIdStr, int Id) {
	        // �õ�������
	        String tableName = className.substring(className.lastIndexOf(".") + 1,
	                className.length());
	        // ��������������Class����
	        Class c = null;
	        try {
	            c = Class.forName(className);
	        } catch (ClassNotFoundException e1) {
	            e1.printStackTrace();
	        }
	        // ƴ�ղ�ѯsql���
	        String sql = "select * from " + tableName + " where " + mainIdStr + "=" + Id;
	        System.out.println("����sql��䣺" + sql);
	        // ������ݿ�����
	        Connection con = ConnectDBFactory.getDBConnection();
	        // �������ʵ��
	        Object obj = null;
	        try {
	            Statement stm = con.createStatement();
	            // �õ�ִ�в�Ѱ��䷵�صĽ����
	            ResultSet set = stm.executeQuery(sql);
	            // �õ�����ķ�������
	            Method[] methods = c.getMethods();
	            // ���������
	            while (set.next()) {
	                obj = c.newInstance();
	                // ��������ķ���
	                for (Method method : methods) {
	                    String methodName = method.getName();
	                    // �������ķ�����set��ͷ
	                    if (methodName.startsWith("set")) {
	                        // ���ݷ������ֵõ����ݱ�����ֶε�����
	                        String columnName = methodName.substring(3,
	                                methodName.length());
	                        // �õ������Ĳ�������
	                        Class[] parmts = method.getParameterTypes();
	                        if (parmts[0] == String.class) {
	                            // �������ΪString���ͣ���ӽ�����а�������ȡ�ö�Ӧ��ֵ������ִ�и�set����
	                            method.invoke(obj, set.getString(columnName));
	                        }
	                        if (parmts[0] == int.class) {
	                            method.invoke(obj, set.getInt(columnName));
	                        }
	                    }
	 
	                }
	            }
	 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return obj;
	    }
}
