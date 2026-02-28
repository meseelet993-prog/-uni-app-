package com.mentalhealth.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class ValidationInterceptor implements HandlerInterceptor {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * 参数验证中间件
     * 验证请求参数的有效性
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 可以在这里添加通用的参数验证逻辑
        String contentType = request.getContentType();
        if (contentType != null && contentType.startsWith("application/json")) {
            // 对JSON请求体进行验证
            // 这里可以根据需要添加具体的验证逻辑
        }
        
        // 示例：验证分页参数
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api/tickets") && "GET".equals(request.getMethod())) {
            String pageParam = request.getParameter("page");
            String pageSizeParam = request.getParameter("pageSize");
            
            if (pageParam != null) {
                try {
                    int page = Integer.parseInt(pageParam);
                    if (page <= 0) {
                        sendErrorResponse(response, 400, "页码必须大于0");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    sendErrorResponse(response, 400, "页码必须是数字");
                    return false;
                }
            }
            
            if (pageSizeParam != null) {
                try {
                    int pageSize = Integer.parseInt(pageSizeParam);
                    if (pageSize <= 0 || pageSize > 100) {
                        sendErrorResponse(response, 400, "每页大小必须在1-100之间");
                        return false;
                    }
                } catch (NumberFormatException e) {
                    sendErrorResponse(response, 400, "每页大小必须是数字");
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private void sendErrorResponse(HttpServletResponse response, int code, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json;charset=UTF-8");
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("code", code);
        errorResponse.put("message", message);
        
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}