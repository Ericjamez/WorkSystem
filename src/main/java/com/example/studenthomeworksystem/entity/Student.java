package com.example.studenthomeworksystem.entity;

import lombok.Data;

@Data
public class Student {
    private Long id;
    private String studentId;
    private String className;
    private String major;
    private String grade;
    private Long classId;
    
    // 关联信息
    private String collegeName;
    private String majorName;
    private String classNameFull;
}
