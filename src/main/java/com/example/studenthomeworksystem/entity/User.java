package com.example.studenthomeworksystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String role; // TEACHER or STUDENT
    private String name;
    private String email;
    private String phone;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // Additional fields for role-specific data
    private String studentId; // For students
    private String teacherId; // For teachers
    private String className; // For students
    private String major; // For students
    private String department; // For teachers
}
