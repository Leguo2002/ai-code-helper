package com.gu01e.aicodehelper.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @className: CorsConfig
 * @Description: TODO
 * @version: v1.0
 * @author: GUOLE
 * @date: 2025/10/10 16:39
 */
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 允许所有来源
                .allowedOrigins("*")
                // 允许所有请求方法
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
                // 允许发送 Cookie
                .allowCredentials(true)
                // 最大响应时间
                .maxAge(3600)
                // 允许所有请求头
                .allowedHeaders("*")
                // 暴露所有请求头
                .exposedHeaders("*");
    }
}