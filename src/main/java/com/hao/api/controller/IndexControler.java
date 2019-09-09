
package com.hao.api.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hao.entity.Users;
import com.hao.service.RedisService;
import com.hao.service.UserService;


@RestController
public class IndexControler {
	@Autowired
	private UserService userService;
	@Autowired
	private RedisService redisService;

	@RequestMapping("/setString")
	public String setString(String key, String value) {
		// redisService.set(key, value, 60l);
		redisService.setString(key, value);
		return "success";
	}

	@RequestMapping("/getString")
	public String getString(String key) {
		return redisService.getString(key);
	}

	@RequestMapping("/setSet")
	public String setSet() {
		Set<String> set = new HashSet<String>();
		set.add("zhangsan");
		set.add("lisi");
		redisService.setSet("setTest", set);
		return "success";
	}

	@RequestMapping("/getUserId")
	public Users getUserId(Long id) {
		return userService.getUser(id);
	}

}
