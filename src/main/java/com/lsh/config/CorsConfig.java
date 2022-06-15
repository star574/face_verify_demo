package com.lsh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @ClassName: CorsConfig
 * @Description: 跨域配置
 * @Date: 2022/6/14 17:08
 * @Author: shihengluo574@gmail.com
 * @Version: 1.0
 */
@Configuration
public class CorsConfig {
	/**
	 * @Description: 当前跨域请求最大有效时长。这里默认1天
	 * @Date: 2022/6/15 15:52
	 **/
	private static final long MAX_AGE = 24 * 60 * 60;

	/**
	 *
	 * @Description: 跨域过滤器
	 * TODO:
	 * @Date: 2022/6/15 15:52
	 * @return: org.springframework.web.filter.CorsFilter
	 **/
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		// 1 设置访问源地址
		corsConfiguration.addAllowedOrigin("*");
		// 2 设置访问源请求头
		corsConfiguration.addAllowedHeader("*");
		// 3 设置访问源请求方法
		corsConfiguration.addAllowedMethod("*");
		corsConfiguration.setMaxAge(MAX_AGE);
		// 4 对接口配置跨域设置
		source.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(source);
	}
}
