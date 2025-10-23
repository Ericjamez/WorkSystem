package com.example.studenthomeworksystem.service;

import com.example.studenthomeworksystem.entity.Class;
import com.example.studenthomeworksystem.mapper.ClassMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassService {

    @Autowired
    private ClassMapper classMapper;

    public List<Class> getAllClasses() {
        return classMapper.findAll();
    }

    public Class getClassById(Long id) {
        return classMapper.findById(id);
    }

    public List<Class> getClassesByMajorId(Long majorId) {
        return classMapper.findByMajorId(majorId);
    }

    public boolean createClass(Class clazz) {
        try {
            // 检查同一专业下班级名称是否已存在
            Class existingClass = classMapper.findByMajorIdAndName(clazz.getMajorId(), clazz.getName());
            if (existingClass != null) {
                return false;
            }
            return classMapper.insert(clazz) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateClass(Class clazz) {
        try {
            // 检查同一专业下班级名称是否与其他班级重复
            Class existingClass = classMapper.findByMajorIdAndName(clazz.getMajorId(), clazz.getName());
            if (existingClass != null && !existingClass.getId().equals(clazz.getId())) {
                return false;
            }
            return classMapper.update(clazz) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteClass(Long id) {
        try {
            return classMapper.delete(id) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
