
package com.hao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@MapperScan("com.hao.mapper")
@EnableCaching
public class AppRedis {

	public static void main(String[] args) {
		SpringApplication.run(AppRedis.class, args);
	}

}
