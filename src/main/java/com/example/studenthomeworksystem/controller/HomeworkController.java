package com.example.studenthomeworksystem.controller;

import com.example.studenthomeworksystem.entity.Homework;
import com.example.studenthomeworksystem.service.HomeworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/homework")
public class HomeworkController {
    
    @Autowired
    private HomeworkService homeworkService;
    
    @GetMapping
    public ResponseEntity<List<Homework>> getAllHomework() {
        List<Homework> homeworkList = homeworkService.getAllHomework();
        return ResponseEntity.ok(homeworkList);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Homework> getHomeworkById(@PathVariable Long id) {
        Homework homework = homeworkService.getHomeworkById(id);
        if (homework != null) {
            return ResponseEntity.ok(homework);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<String> createHomework(@RequestBody Homework homework) {
        boolean success = homeworkService.createHomework(homework);
        if (success) {
            return ResponseEntity.ok("作业创建成功");
        } else {
            return ResponseEntity.badRequest().body("作业创建失败");
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<String> updateHomework(@PathVariable Long id, @RequestBody Homework homework) {
        homework.setId(id);
        boolean success = homeworkService.updateHomework(homework);
        if (success) {
            return ResponseEntity.ok("作业更新成功");
        } else {
            return ResponseEntity.badRequest().body("作业更新失败");
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHomework(@PathVariable Long id) {
        boolean success = homeworkService.deleteHomework(id);
        if (success) {
            return ResponseEntity.ok("作业删除成功");
        } else {
            return ResponseEntity.badRequest().body("作业删除失败");
        }
    }
    
    @GetMapping("/course/{courseName}")
    public ResponseEntity<List<Homework>> getHomeworkByCourseName(@PathVariable String courseName) {
        List<Homework> homeworkList = homeworkService.getHomeworkByCourseName(courseName);
        return ResponseEntity.ok(homeworkList);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<Homework>> getActiveHomework() {
        List<Homework> activeHomework = homeworkService.getActiveHomework();
        return ResponseEntity.ok(activeHomework);
    }
}
