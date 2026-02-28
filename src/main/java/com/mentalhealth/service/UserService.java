package com.mentalhealth.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mentalhealth.entity.User;
import com.mentalhealth.mapper.UserMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    /**
     * 用户登录
     */
    public User login(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username).eq("password", password);
        User user = this.getOne(queryWrapper);
        if (user != null) {
            // 确保用户角色不为null
            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("student");
            }
            return user;
        }
        return null;
    }

    /**
     * 用户注册
     */
    public User register(User user) {
        // 检查用户名是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        if (this.count(queryWrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 设置默认值
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        
        // 设置默认角色
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("student");
        }
        
        // 设置创建时间
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }

        this.save(user);
        return user;
    }

    /**
     * 根据ID获取用户
     */
    public User getUserById(Long id) {
        User user = this.getById(id);
        if (user != null) {
            // 确保用户角色不为null
            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("student");
            }
            return user;
        }
        return null;
    }

    /**
     * 获取所有用户
     */
    public List<User> getAllUsers() {
        List<User> users = this.list();
        // 确保所有用户角色都不为null
        for (User user : users) {
            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("student");
            }
        }
        return users;
    }

    /**
     * 更新用户信息
     */
    public User updateUser(User user) {
        this.updateById(user);
        return user;
    }

    /**
     * 删除用户
     */
    public void deleteUser(Long id) {
        this.removeById(id);
    }
}