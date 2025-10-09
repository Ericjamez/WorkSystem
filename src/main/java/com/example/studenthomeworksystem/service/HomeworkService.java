package com.example.studenthomeworksystem.service;

import com.example.studenthomeworksystem.entity.Homework;
import com.example.studenthomeworksystem.mapper.HomeworkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeworkService {
    
    @Autowired
    private HomeworkMapper homeworkMapper;
    
    public List<Homework> getAllHomework() {
        return homeworkMapper.findAll();
    }
    
    public Homework getHomeworkById(Long id) {
        return homeworkMapper.findById(id);
    }
    
    public boolean createHomework(Homework homework) {
        try {
            homeworkMapper.insert(homework);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateHomework(Homework homework) {
        try {
            int result = homeworkMapper.update(homework);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteHomework(Long id) {
        try {
            int result = homeworkMapper.delete(id);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Homework> getHomeworkByCourseName(String courseName) {
        return homeworkMapper.findByCourseName(courseName);
    }
    
    public List<Homework> getActiveHomework() {
        return homeworkMapper.findActiveHomework();
    }
}
