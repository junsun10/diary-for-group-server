package com.example.diary.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 허용할 경로 설정
                .allowedOrigins("*") // 허용할 오리진 설정
                .allowedMethods("*") // 허용할 메소드 설정
                .allowedHeaders("*"); // 허용할 헤더 설정
    }
}
