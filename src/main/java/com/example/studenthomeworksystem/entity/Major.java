package com.example.studenthomeworksystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Major {
    private Long id;
    private Long collegeId;
    private String name;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 关联信息
    private String collegeName;
}
