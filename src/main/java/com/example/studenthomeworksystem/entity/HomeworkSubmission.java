package com.example.studenthomeworksystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HomeworkSubmission {
    private Long id;
    private Long studentId;
    private Long homeworkId;
    private String content;
    private String attachmentPath;
    private Integer score;
    private String teacherComment;
    private LocalDateTime submitTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // Additional fields for join queries
    private String studentName;
    private String studentIdNumber;
    private String homeworkTitle;
    private String courseName;
}
