package com.sfpy.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sfpy.db.BizDB;
import com.sfpy.entity.ResultInfo;
import com.sfpy.entity.TB_SFPY_USER;

@Service
public class UserService {

	public List<TB_SFPY_USER> getAllUser() {
		return null;
	}
	
	public void addUser(TB_SFPY_USER user) {

	}

	public void updateUser(String userId) {
		
	}

	public void delelteUser(String userId) {

	}
	
	public ResultInfo checkLogin(String userName, String password) {
		ResultInfo result = new ResultInfo();
		StringBuffer cond = new StringBuffer();
		cond.append(TB_SFPY_USER.USER_NAME.toSqlEQ(userName))
			.append(" AND ")
			.append(TB_SFPY_USER.USER_PWD.toSqlEQ(password));
		
		try {
			 List<Map<String, Object>> userList = BizDB.getInstance().searchAsMapList(TB_SFPY_USER.TABLE, TB_SFPY_USER.ALL_FIELDS, cond.toString());
			if(userList != null && !userList.isEmpty()) {
				Map<String, Object> dataMap = userList.get(0);
				Object userId = dataMap.get(TB_SFPY_USER.USER_ID.name);
				if(userId != null) {
					System.out.println("登陆成功");
					result.setStatus(10);
					result.setMsg("登陆成功！");
					result.setData(userId);;
				}
			} else {
				System.out.println("驗證失敗！");
				result.setStatus(-1);
				result.setMsg("");
			}
		} catch (Exception e) {
		}
		return result;
	}

}
