package com.example.studenthomeworksystem.service;

import com.example.studenthomeworksystem.entity.College;
import com.example.studenthomeworksystem.mapper.CollegeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollegeService {

    @Autowired
    private CollegeMapper collegeMapper;

    public List<College> getAllColleges() {
        return collegeMapper.findAll();
    }

    public College getCollegeById(Long id) {
        return collegeMapper.findById(id);
    }

    public boolean createCollege(College college) {
        try {
            // 检查学院名称是否已存在
            College existingCollege = collegeMapper.findByName(college.getName());
            if (existingCollege != null) {
                return false;
            }
            return collegeMapper.insert(college) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCollege(College college) {
        try {
            // 检查学院名称是否与其他学院重复
            College existingCollege = collegeMapper.findByName(college.getName());
            if (existingCollege != null && !existingCollege.getId().equals(college.getId())) {
                return false;
            }
            return collegeMapper.update(college) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCollege(Long id) {
        try {
            return collegeMapper.delete(id) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
