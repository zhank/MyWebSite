package com.sfpy.entity;

import com.sfpy.field.AbstractTable;
import com.sfpy.field.Field;
import com.sfpy.type.IntType;
import com.sfpy.type.StringType;

/**
 * 人员基础信息
 * @author SFPY
 */
public class TB_SFPY_USER extends AbstractTable {
	public static String TABLE = "TB_SFPY_USER";

	public static Field USER_ID = new Field("用户ID", "USER_ID", new IntType(), true, true);
	public static Field USER_NAME = new Field("用户名", "USER_NAME", new StringType());
	public static Field USER_SEX = new Field("用户ID", "USER_SEX", new IntType());
	public static Field USER_AGE = new Field("用户ID", "USER_AGE", new IntType());
	public static Field USER_STATUS = new Field("用户ID", "USER_STATUS", new IntType());
	public static Field USER_PWD = new Field("用户ID", "USER_PWD", new StringType());
	public static Field USER_ADDRESS = new Field("用户ID", "USER_ADDRESS", new StringType());
	public static Field USER_EMAIL = new Field("用户ID", "USER_EMAIL", new StringType());
	
	public static final Field[] ALL_FIELDS = new TB_SFPY_USER().getArrayTableFields();
}
