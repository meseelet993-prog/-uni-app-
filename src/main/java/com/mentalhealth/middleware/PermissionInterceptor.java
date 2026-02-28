package com.mentalhealth.middleware;

import com.mentalhealth.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class PermissionInterceptor implements HandlerInterceptor {
    
    /**
     * 权限检查中间件
     * 检查用户是否有权限访问特定资源
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从request中获取当前用户
        User currentUser = (User) request.getAttribute("currentUser");
        if (currentUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\": 401, \"message\": \"用户未登录\"}");
            return false;
        }
        
        // 获取请求路径和方法
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        
        // 根据用户角色和请求路径判断权限
        String role = currentUser.getRole();
        if (role == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"code\": 403, \"message\": \"用户角色未定义\"}");
            return false;
        }
        
        // 对于工单相关接口的权限控制
        if (requestURI.startsWith("/api/tickets")) {
            // 学生只能访问自己的工单
            if ("student".equals(role)) {
                // 学生可以创建工单
                if (requestURI.equals("/api/tickets") && "POST".equals(method)) {
                    return true;
                }
                // 学生可以查看自己的工单列表
                else if (requestURI.equals("/api/tickets") && "GET".equals(method)) {
                    return true;
                }
                // 学生可以查看、更新自己工单的状态、删除自己的工单
                else if (requestURI.matches("/api/tickets/\\d+") && 
                         ("GET".equals(method) || "PUT".equals(method) || "DELETE".equals(method))) {
                    return true;
                } else {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("{\"code\": 403, \"message\": \"权限不足\"}");
                    return false;
                }
            }
            // 咨询师可以查看和处理分配给自己的工单
            else if ("consultant".equals(role)) {
                return true;
            }
            // 管理员可以访问所有工单接口
            else if ("admin".equals(role)) {
                return true;
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("{\"code\": 403, \"message\": \"权限不足\"}");
                return false;
            }
        }
        
        // 其他接口默认允许访问
        return true;
    }
}