package com.mentalhealth.middleware;

import com.mentalhealth.entity.User;
import com.mentalhealth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    
    @Autowired
    private UserService userService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        
        // 对于创建工单的POST请求，允许匿名访问
        if ("/api/tickets".equals(requestURI) && "POST".equals(method)) {
            return true;
        }
        
        // 对于获取工单列表的POST请求，允许匿名访问
        if ("/api/tickets/list".equals(requestURI) && "POST".equals(method)) {
            return true;
        }
        
        // 对于健康检查接口，允许匿名访问
        if ("/api/health".equals(requestURI)) {
            return true;
        }
        
        // 对于根路径和API路径，允许匿名访问
        if ("/api".equals(requestURI) || "/".equals(requestURI)) {
            return true;
        }
        
        // 获取会话
        HttpSession session = request.getSession(false);
        
        // 检查是否有会话
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\": 401, \"message\": \"用户未登录\"}");
            return false;
        }
        
        // 检查会话中是否有用户信息
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\": 401, \"message\": \"用户未登录\"}");
            return false;
        }
        
        // 将用户信息放入request属性中供后续使用
        request.setAttribute("currentUser", user);
        
        return true;
    }
}