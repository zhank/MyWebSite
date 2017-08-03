package com.sfpy.controller;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sfpy.entity.ResultInfo;
import com.sfpy.service.UserService;

@Controller
@RequestMapping("/user")
public class LoginController {

	@Resource
	private UserService userService;
	
	@RequestMapping("/login.do")
	@ResponseBody
	public ResultInfo execute(String userName, String password) {
		ResultInfo resultInfo = userService.checkLogin(userName, password);
		return resultInfo;
	}

}
