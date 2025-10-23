package com.example.studenthomeworksystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class College {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
