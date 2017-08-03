package com.sfpy.type;

import java.io.Serializable;
import java.util.Map;

public abstract class ValidateType implements Serializable{
		
	private static final long serialVersionUID = 1L;

	// fValidate校验错误时使用的提示信息,如果不设置该字段,则使用默认的提示信息
	protected String message = null;

	// 是否允许为空的标志
	protected boolean notNull = false;

	// 允许输入的最大长度
	protected int maxLength = 65535;

	/**
	 * 缺省构造函数
	 * 
	 */
	public ValidateType() {
		super();
	}

	public ValidateType(String message) {
		super();
		this.message = message;
	}

	/**
	 * 获取当前类型的数字标识，其值是java.sql.Type中定义的类型
	 * 
	 * @return
	 */
	abstract public int getSqlType();

	/**
	 * 获取验证属性，子类重载该方法返回具体的校验代码，目前客户端使用jQuery Validate，所以子类都与此js库相关
	 * 
	 * @return
	 */
	public Map<String, String> getValidateAttrs() {
		return null;
	}

	/**
	 * 获取客户端校验代码，子类重载该方法返回具体的校验代码，目前客户端使用fValidate（一个js库），所以子类都与此js库相关
	 * 
	 * @param notNull
	 *            是否非空
	 * @return 校验代码
	 */
	abstract public String getValidateCode(boolean notNull);

	/**
	 * 获取当fValidate校验出错时使用的提示信息
	 * 
	 * @return
	 */
	public String getValideteMsg() {
		return message;
	}

	/**
	 * 获取是否允许为空
	 * 
	 * @return 不允许为空时返回true,否则返回false
	 */
	public boolean notNull() {
		return notNull;
	}

	/**
	 * 设置是否允许为空
	 * 
	 * @param 不允许为空时置为true,否则置为false
	 * @return
	 */

	public final ValidateType setNotNull(boolean notNull) {
		this.notNull = notNull;
		return this;
	}

	/**
	 * 设置允许输入的最大长度
	 */

	public final ValidateType setMaxLength(int maxLength) {
		this.maxLength = maxLength;
		return this;
	}

	/**
	 * 获取允许输入的最大长度
	 */
	public int getMaxLength() {
		return maxLength;
	}
}
