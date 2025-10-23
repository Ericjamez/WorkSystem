package com.example.studenthomeworksystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Class {
    private Long id;
    private Long majorId;
    private String name;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 关联信息
    private String majorName;
    private String collegeName;
    private Long collegeId;
}
