package com.hao.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hao.entity.Users;
import com.hao.mapper.UserMapper;
import com.hao.util.EhCacheUtils;


@Service
public class UserService {
	@Autowired
	private EhCacheUtils ehCacheUtils;
	@Autowired
	private RedisService redisService;
	@Autowired
	private UserMapper userMapper;
	private String cacheName = "userCache";

	public Users getUser(Long id) {
		// 1.先查询一级缓存 key 以 当前的类名+方法名称+id +参数值FD
		String key = this.getClass().getName() + "-" + Thread.currentThread().getStackTrace()[1].getMethodName()
				+ "-id:" + id;
		// 1.1 查询一级缓存数据有对应的值存在，如果存在直接返回
		Users users = (Users) ehCacheUtils.get(cacheName, key);
		if (users != null) {
			System.out.println("key:" + key + ",从一级缓存获取数据:users:" + users.toString());
			return users;
		}
		// 1.1 查询一级缓存数据没有对应的值存在，直接查询二级缓存redis redis 中如何存放对象呢？ json格式
		// 2.查询二级缓存
		String userJSON = redisService.getString(key);
		// 如果redis缓存中有这个对应的 值，修改一级缓存
		if (!StringUtils.isEmpty(userJSON)) {
			JSONObject jsonObject = new JSONObject();
			Users resultUser = jsonObject.parseObject(userJSON, Users.class);
			// 存放在一级缓存
			ehCacheUtils.put(cacheName, key, resultUser);
			return resultUser;
		}
		// 3.查询db 数据库
		Users user = userMapper.getUser(id);
		if (user == null) {
			return null;
		}
		// 如何保证 两级缓存有效期相同 时差问题
		// 存放二级缓存redis
		redisService.setString(key, new JSONObject().toJSONString(user));
		// 存放在一级缓存
		ehCacheUtils.put(cacheName, key, user);
		// 一级缓存的有效期时间减去二级缓存执行代码时间

		return user;
	}

}
