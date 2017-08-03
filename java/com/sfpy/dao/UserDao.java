package com.sfpy.dao;

import java.util.List;

import com.sfpy.entity.TB_SFPY_USER;

public interface UserDao {

	public List<TB_SFPY_USER> getAllUser();
	
	public TB_SFPY_USER getUser(String userName);
	
	public void addUser(TB_SFPY_USER user);

	public void updateUser(String userId);

	public void delelteUser(String userId);

}
