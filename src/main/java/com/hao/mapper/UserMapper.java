
package com.hao.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheConfig;

import com.hao.entity.Users;



@CacheConfig(cacheNames = { "userCache" })
public interface UserMapper {
	@Select("SELECT ID ,NAME,AGE FROM member where id=#{id}")
	Users getUser(@Param("id") Long id);
	// @CacheConfig 配置缓存基本信息cacheNames缓存名称
	// @Cacheable 该方法查询数据库完毕之后，存入到缓存
}
