package com.lsh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author star
 */
@SpringBootApplication
@MapperScan(value = {"com.lsh.mapper"})
@EnableConfigurationProperties
public class FaceVerifyDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(FaceVerifyDemoApplication.class, args);
	}

}
