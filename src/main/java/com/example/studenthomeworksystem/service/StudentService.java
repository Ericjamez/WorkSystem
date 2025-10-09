package com.example.studenthomeworksystem.service;

import com.example.studenthomeworksystem.entity.Student;
import com.example.studenthomeworksystem.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    
    @Autowired
    private StudentMapper studentMapper;
    
    public List<Student> getAllStudents() {
        return studentMapper.findAll();
    }
    
    public Student getStudentById(Long id) {
        return studentMapper.findById(id);
    }
    
    public Student getStudentByStudentId(String studentId) {
        return studentMapper.findByStudentId(studentId);
    }
    
    public boolean createStudent(Student student) {
        try {
            studentMapper.insert(student);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateStudent(Student student) {
        try {
            int result = studentMapper.update(student);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteStudent(Long id) {
        try {
            int result = studentMapper.delete(id);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Student> getStudentsByClassName(String className) {
        return studentMapper.findByClassName(className);
    }
}
