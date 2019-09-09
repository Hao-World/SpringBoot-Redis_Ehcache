
package com.hao.service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;



@Component
public class RedisService {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	// public void set(String key, Object object, Long time) {
	// stringRedisTemplate.opsForValue();
	// // 存放String 类型
	// if (object instanceof String) {
	// setString(key, object);
	// }
	// // 存放 set类型
	// if (object instanceof Set) {
	// setSet(key, object);
	// }
	// // 设置有效期 以秒为单位
	// stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
	// }
	//
	public void setString(String key, Object object) {
		// 开启事务权限
		// stringRedisTemplate.setEnableTransactionSupport(true);
		try {
			// 开启事务 begin
			// stringRedisTemplate.multi();
			String value = (String) object;
			stringRedisTemplate.opsForValue().set(key, value);
			System.out.println("存入完毕,马上开始提交redis事务");
			// 提交事务
			// stringRedisTemplate.exec();
		} catch (Exception e) {
			// 需要回滚事务
			// stringRedisTemplate.discard();
		}
	}

	public void setSet(String key, Object object) {
		Set<String> value = (Set<String>) object;
		for (String oj : value) {
			stringRedisTemplate.opsForSet().add(key, oj);
		}
	}

	public String getString(String key) {
		return stringRedisTemplate.opsForValue().get(key);
	}

}
