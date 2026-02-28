package com.mentalhealth.config;

import com.mentalhealth.middleware.AuthInterceptor;
import com.mentalhealth.middleware.PermissionInterceptor;
import com.mentalhealth.middleware.ValidationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    
    @Autowired
    private AuthInterceptor authInterceptor;
    
    @Autowired
    private PermissionInterceptor permissionInterceptor;
    
    @Autowired
    private ValidationInterceptor validationInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册身份验证拦截器
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/tickets/**")
                .addPathPatterns("/api/upload");
        
        // 注册权限检查拦截器
        registry.addInterceptor(permissionInterceptor)
                .addPathPatterns("/api/tickets/**");
        
        // 注册参数验证拦截器
        registry.addInterceptor(validationInterceptor)
                .addPathPatterns("/api/tickets/**")
                .addPathPatterns("/api/upload");
    }
}