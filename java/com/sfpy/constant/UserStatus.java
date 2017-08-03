package com.sfpy.constant;

public enum UserStatus {
	NORMAL(10, "Õý³£"), DELETE(-1, "×¢Ïú");

	int id;
	String desc;

	private UserStatus(int id, String desc) {
		this.id = id;
		this.desc = desc;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
