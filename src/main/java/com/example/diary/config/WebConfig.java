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
                .excludePathPatterns("/", "/members/new", "/members/login", "/members/logout", "/members/login-status",
                        "/css/**", "/*.ico", "/error");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 허용할 경로 설정
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 메소드 설정
                .allowedOrigins("https://www.diary-for-group.kro.kr", "http://localhost:80", "http://localhost:8081") // 허용할 오리진 설정
                .allowedHeaders("*") // 허용할 헤더 설정
                .allowCredentials(true) // 인증 정보 허용
                .maxAge(1800); // preflight 요청 캐시 시간(초)
    }
}

