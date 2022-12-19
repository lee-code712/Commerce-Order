package com.digital.v3.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.digital.v3.interceptor.AuthInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
    public void addInterceptors(InterceptorRegistry registry) {
		
		// 인가 처리를 위한 인터셉터 설정
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns(AuthInterceptor.authEssential)
                .excludePathPatterns(AuthInterceptor.authInessential);
    }
	
}
