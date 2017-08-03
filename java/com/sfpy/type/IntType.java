package com.sfpy.type;

import java.util.HashMap;
import java.util.Map;

public class IntType extends ValidateType {

	private static final long serialVersionUID = 1L;

	// 校验代码
	private String vcode = null;

	private final int min, max;
	
	public IntType() {
		this(Integer.MIN_VALUE, Integer.MAX_VALUE, null);
	}

	public IntType(String message) {
		this(Integer.MIN_VALUE, Integer.MAX_VALUE, message);
	}

	public IntType(int min, int max) {
		this(min, max, null);
	}

	public IntType(int min, int max, String message) {
		super(message);
		this.min = min;
		this.max = max;
		updateCode(min, max);
	}

	@Override
	public int getSqlType() {
		return java.sql.Types.INTEGER;
	}

	/**
	 * 获取fValidate使用的校验码
	 * 
	 * @return
	 */
	@Override
	public String getValidateCode(boolean notNull) {
		return notNull ? vcode : vcode + "|bok";
	}

	private void updateCode(int min, int max) {
		StringBuffer code = new StringBuffer(32);
		code.append("number|0");
		if (min >= Integer.MIN_VALUE && max <= Integer.MAX_VALUE && min <= max) {
			code.append("|" + min + "|" + max);
		}

		vcode = code.toString();
	}
	
	@Override
	public Map<String, String> getValidateAttrs() {
		Map<String, String> valAttrMap = new HashMap<String, String>();
		valAttrMap.put("type", "integer");
		valAttrMap.put("range", "["+ min + "," + max + "]");
		return valAttrMap;
	}
}
