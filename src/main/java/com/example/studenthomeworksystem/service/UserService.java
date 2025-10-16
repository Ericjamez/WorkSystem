package com.example.studenthomeworksystem.service;

import com.example.studenthomeworksystem.entity.User;
import com.example.studenthomeworksystem.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    public User authenticate(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
    
    public User findById(Long id) {
        return userMapper.findById(id);
    }
    
    public List<User> findAllStudents() {
        return userMapper.findAllStudents();
    }
    
    public List<User> findAllTeachers() {
        return userMapper.findAllTeachers();
    }
    
    public boolean registerStudent(User user) {
        try {
            user.setRole("STUDENT");
            userMapper.insertUser(user);
            userMapper.insertStudent(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean registerTeacher(User user) {
        try {
            user.setRole("TEACHER");
            userMapper.insertUser(user);
            userMapper.insertTeacher(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
