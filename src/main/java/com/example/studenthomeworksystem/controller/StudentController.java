package com.example.studenthomeworksystem.controller;

import com.example.studenthomeworksystem.entity.Student;
import com.example.studenthomeworksystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        if (student != null) {
            return ResponseEntity.ok(student);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/student-id/{studentId}")
    public ResponseEntity<Student> getStudentByStudentId(@PathVariable String studentId) {
        Student student = studentService.getStudentByStudentId(studentId);
        if (student != null) {
            return ResponseEntity.ok(student);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<String> createStudent(@RequestBody Student student) {
        boolean success = studentService.createStudent(student);
        if (success) {
            return ResponseEntity.ok("学生创建成功");
        } else {
            return ResponseEntity.badRequest().body("学生创建失败");
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<String> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        student.setId(id);
        boolean success = studentService.updateStudent(student);
        if (success) {
            return ResponseEntity.ok("学生信息更新成功");
        } else {
            return ResponseEntity.badRequest().body("学生信息更新失败");
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        boolean success = studentService.deleteStudent(id);
        if (success) {
            return ResponseEntity.ok("学生删除成功");
        } else {
            return ResponseEntity.badRequest().body("学生删除失败");
        }
    }
    
    @GetMapping("/class/{className}")
    public ResponseEntity<List<Student>> getStudentsByClassName(@PathVariable String className) {
        List<Student> students = studentService.getStudentsByClassName(className);
        return ResponseEntity.ok(students);
    }
}
