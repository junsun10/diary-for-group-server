package com.example.diary.config;

import com.example.diary.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/members/new", "/members/login", "/members/logout",
                        "/css/**", "/*.ico", "/error");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 허용할 경로 설정
                .allowedOrigins("http://localhost:8081") // 허용할 오리진 설정
                .allowedMethods("*") // 허용할 메소드 설정
                .allowCredentials(true) // 인증 정보 허용
                .allowedHeaders("*"); // 허용할 헤더 설정
    }
}
