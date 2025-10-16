package com.example.studenthomeworksystem.controller;

import com.example.studenthomeworksystem.entity.Homework;
import com.example.studenthomeworksystem.service.HomeworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/homework")
public class HomeworkController {
    
    @Autowired
    private HomeworkService homeworkService;
    
    @GetMapping
    public Map<String, Object> getAllHomework() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Homework> homeworkList = homeworkService.getAllHomework();
            response.put("success", true);
            response.put("data", homeworkList);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取作业列表失败");
        }
        
        return response;
    }
    
    @GetMapping("/teacher/{teacherId}")
    public Map<String, Object> getHomeworkByTeacher(@PathVariable Long teacherId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Homework> homeworkList = homeworkService.getHomeworkByTeacherId(teacherId);
            response.put("success", true);
            response.put("data", homeworkList);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取教师作业列表失败");
        }
        
        return response;
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
