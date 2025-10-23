package com.example.studenthomeworksystem.controller;

import com.example.studenthomeworksystem.entity.User;
import com.example.studenthomeworksystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String studentId = loginData.get("studentId");
        String teacherId = loginData.get("teacherId");
        String password = loginData.get("password");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            User user = null;
            
            // 优先使用学号或教师编号登录
            if (studentId != null && !studentId.trim().isEmpty()) {
                user = userService.authenticateByStudentId(studentId, password);
            } else if (teacherId != null && !teacherId.trim().isEmpty()) {
                user = userService.authenticateByTeacherId(teacherId, password);
            } else if (username != null && !username.trim().isEmpty()) {
                // 保留原有的用户名登录方式（主要用于管理员）
                user = userService.authenticate(username, password);
            }
            
            if (user != null) {
                response.put("success", true);
                response.put("user", user);
                response.put("message", "登录成功");
            } else {
                response.put("success", false);
                response.put("message", "用户名或密码错误");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "登录失败：" + e.getMessage());
            // 记录详细错误信息用于调试
            System.err.println("登录异常: " + e.getMessage());
            e.printStackTrace();
        }
        
        return response;
    }
    
    @PostMapping("/register/student")
    public Map<String, Object> registerStudent(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        
        boolean success = userService.registerStudent(user);
        if (success) {
            response.put("success", true);
            response.put("message", "学生注册成功");
        } else {
            response.put("success", false);
            response.put("message", "注册失败，用户名可能已存在");
        }
        
        return response;
    }
    
    @PostMapping("/register/teacher")
    public Map<String, Object> registerTeacher(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        
        boolean success = userService.registerTeacher(user);
        if (success) {
            response.put("success", true);
            response.put("message", "教师注册成功");
        } else {
            response.put("success", false);
            response.put("message", "注册失败，用户名可能已存在");
        }
        
        return response;
    }
}
