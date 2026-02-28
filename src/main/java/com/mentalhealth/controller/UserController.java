package com.mentalhealth.controller;

import com.mentalhealth.entity.User;
import com.mentalhealth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        List<User> users = userService.getAllUsers();
        // 确保所有用户的角色都不为null
        for (User user : users) {
            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("student");
            }
        }
        return users;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        // 确保角色不为null
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("student");
        }
        return userService.register(user);
    }
}