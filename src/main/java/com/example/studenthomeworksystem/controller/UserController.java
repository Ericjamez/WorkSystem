package com.example.studenthomeworksystem.controller;

import com.example.studenthomeworksystem.entity.User;
import com.example.studenthomeworksystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/students")
    public Map<String, Object> getAllStudents() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<User> students = userService.findAllStudents();
            response.put("success", true);
            response.put("data", students);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取学生列表失败");
        }
        
        return response;
    }
    
    @GetMapping("/teachers")
    public Map<String, Object> getAllTeachers() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<User> teachers = userService.findAllTeachers();
            response.put("success", true);
            response.put("data", teachers);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取教师列表失败");
        }
        
        return response;
    }
    
    @GetMapping("/{id}")
    public Map<String, Object> getUserById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            User user = userService.findById(id);
            if (user != null) {
                response.put("success", true);
                response.put("data", user);
            } else {
                response.put("success", false);
                response.put("message", "用户不存在");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取用户信息失败");
        }
        
        return response;
    }
}
