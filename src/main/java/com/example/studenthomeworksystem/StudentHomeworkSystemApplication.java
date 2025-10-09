package com.example.studenthomeworksystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.studenthomeworksystem.mapper")
public class StudentHomeworkSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentHomeworkSystemApplication.class, args);
    }
}
