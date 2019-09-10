package com.hao.service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.hao.entity.Users;
import com.hao.mapper.UserMapper;


/**
 * 
 * @author hao
 *
 */
public class UserCacheService {

	@Autowired
	private RedisService redisService;
	@Autowired
	private UserMapper userMapper;
	
	private Lock lock = new ReentrantLock();
	private String SIGN_KEY = "${NULL}";
	
	/**
	 * 演示利用锁机制解决缓存雪崩问题
	 * @param id
	 * @return
	 */
	 @RequestMapping("/getUsers")
 	public Users getByUsers(Long id) {
 		// 1.先查询redis
 		String key = this.getClass().getName() + "-" + Thread.currentThread().getStackTrace()[1].getMethodName()
 				+ "-id:" + id;
 		String userJson = redisService.getString(key);
 		if (!StringUtils.isEmpty(userJson)) {
 			Users users = JSONObject.parseObject(userJson, Users.class);
 			return users;
 		}
 		Users user = null;
 		try {
 			lock.lock();
 			// 查询db
 			user = userMapper.getUser(id);
 			redisService.setSet(key, JSONObject.toJSONString(user));
 		} catch (Exception e) {

		} finally {
			lock.unlock(); // 释放锁
		}
		return user;
	}
	 
	 /**
	  * 解决穿透问题
	  * @param id
	  * @return
	  */
	 public String getByUsers2(Long id) {
 		// 1.先查询redis
 		String key = this.getClass().getName() + "-" + Thread.currentThread().getStackTrace()[1].getMethodName()
 				+ "-id:" + id;
 		String userName = redisService.getString(key);
 		if (!StringUtils.isEmpty(userName)) {
 			return userName;
 		}
 		System.out.println("######开始发送数据库DB请求########");
 		Users user = userMapper.getUser(id);
 		String value = null;
 		if (user == null) {
 			// 标识为null
 			value = SIGN_KEY;
 		} else {
 			value = user.getName();
 		}
 		redisService.setString(key, value);
 		return value;
 	}

	
}
