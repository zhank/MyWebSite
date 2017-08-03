package com.sfpy.field;


import java.io.Serializable;
import java.sql.Types;

import com.sfpy.type.ValidateType;

/**
 * Field代表确定属性的数据的定义，属性包括该数据的内部标识（name）、类型（type）、界面显示名称（caption）、是否非空（notNull）等。
 * 
 * <p>
 * Field的最初概念是用来代表数据库中的字段，所以有name（字段名），caption（界面显示的名称，类似于VCL中的caption），notnull（是否非空），
 * 以及Type（数据类型，是数据库类型的扩展，包括字符串、整数、邮件、IP地址、端口等）。后来配置部分以及一些临时数据也用Field来描述，所以它不
 * 仅仅限于代表数据库字段，而应该是表示数据标识(name)、显示名称(caption)、类型（用于验证）的类，这些属性经常一同出现，所以用一个类来代表。
 * 
 * <p>
 * 抽象出Field的另外一个原因，是为了在系统中有一个集中的地方统一定义系统中所用到的确定数据类型（类似于数据字典概念），这些数据可能是数据库字段，
 * 也可能是配置文件中的数据，所有这种数据在系统中应该<b>唯一</b>地定义，任何需要使用的地方都引用该定义，不能重复定义。
 * 
 * <p>
 * 一般在使用时，应该把Field作为静态类的静态成员，如定义一个Admin数据表类时：
 * 
 * <pre>
 *    public class TbAdmin {
 *        public static final Field SUBJECT_CN = new Field(&quot;人员标识&quot;, &quot;SUBJECT_CN&quot;, new StringType(32), true, true);
 *        ...
 *    };
 * </pre>
 * 
 * @author zhangk
 * @version 1.0
 * @since 17
 */
public class Field implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用于界面显示字符串
	 */
	public final String caption;

	/**
	 * 内部使用的标识，对于数据库字段，也是字段名称
	 */
	public String name;

	/**
	 * 是否非空
	 */
	public final boolean notNull;

	/**
	 * 是否为主键
	 */
	public final boolean isPrimaryKey;

	/**
	 * 字段对应的校验类型
	 */
	private ValidateType type;
	
	/**
	 * 构造函数
	 * 
	 * @param caption	界面显示的名称
	 * @param name		内部数据标识，对于数据库字段是字段名称
	 * @param type		可验证的数据类型
	 */
	public Field(String caption, String name, ValidateType type) {
		this(caption, name, type, false, false);
	}

	/**
	 * 构造函数
	 * 
	 * @param caption	界面显示的名称
	 * @param name		内部数据标识，对于数据库字段是字段名称
	 * @param type		可验证的数据类型
	 * @param notNull	是否非空
	 */
	public Field(String caption, String name, ValidateType type, boolean notNull) {
		this(caption, name, type, notNull, false);
	}

	/**
	 * 构造函数
	 * 
	 * @param caption		界面显示的名称
	 * @param name			内部数据标识，对于数据库字段是字段名称
	 * @param type			可验证的数据类型
	 * @param notNull		是否非空
	 * @param isPrimaryKey	是否为主键，一般定义数据库表时用到
	 */
	public Field(String caption, String name, ValidateType type, boolean notNull, boolean isPrimaryKey) {
		this.caption = caption;
		this.name = name;
		this.notNull = notNull;
		this.type = type;
		this.isPrimaryKey = isPrimaryKey;

		if (type != null) {
			type.setNotNull(notNull);
		}
	}

	/**
	 * 获取对应的验证类型
	 */
	public ValidateType getType() {
		return type;
	}

	/**
	 * 根据field的类型，将val转换为name=val的sql格式，目前只支持string和integer类型 是为了方便写简单的查询条件
	 * 
	 * @param val 需要转换为sql语句的数据
	 * @return 转换后的sql字符串
	 */
	public String toSqlEQ(Object val) {
		if (val == null)
			return null;

		switch (getType().getSqlType()) {
		case Types.VARCHAR:
			return name + "='" + val + "'";
		case Types.INTEGER:
		case Types.NUMERIC:
			return name + "=" + val;
		default:
			throw new UnsupportedOperationException("toSqlEQ不支持该类型");
		}
	}
	
	

}

