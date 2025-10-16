package com.example.studenthomeworksystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Homework {
    private Long id;
    private Long teacherId;
    private String title;
    private String description;
    private String courseName;
    private LocalDateTime deadline;
    private Integer totalScore;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // Additional fields for display
    private String teacherName;
}
