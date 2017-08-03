package com.sfpy.test;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sfpy.dao.UserDao;
import com.sfpy.db.BizDB;
import com.sfpy.entity.TB_SFPY_USER;

public class test {
	public static void main(String[] args) {	
		/*ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-mvc.xml");
		UserDao userDao = ctx.getBean(UserDao.class);
		User user = userDao.getUser("1234");
		System.out.println(user.getPassword());*/
		try {
			int count = BizDB.getInstance().getCount(TB_SFPY_USER.TABLE);
			System.out.println("人员个数：" + count);
		} catch (Exception e) {
			
		}
	}
}
