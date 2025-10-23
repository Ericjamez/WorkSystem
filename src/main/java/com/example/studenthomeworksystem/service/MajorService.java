package com.example.studenthomeworksystem.service;

import com.example.studenthomeworksystem.entity.Major;
import com.example.studenthomeworksystem.mapper.MajorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MajorService {

    @Autowired
    private MajorMapper majorMapper;

    public List<Major> getAllMajors() {
        return majorMapper.findAll();
    }

    public Major getMajorById(Long id) {
        return majorMapper.findById(id);
    }

    public List<Major> getMajorsByCollegeId(Long collegeId) {
        return majorMapper.findByCollegeId(collegeId);
    }

    public boolean createMajor(Major major) {
        try {
            // 检查同一学院下专业名称是否已存在
            Major existingMajor = majorMapper.findByCollegeIdAndName(major.getCollegeId(), major.getName());
            if (existingMajor != null) {
                return false;
            }
            return majorMapper.insert(major) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMajor(Major major) {
        try {
            // 检查同一学院下专业名称是否与其他专业重复
            Major existingMajor = majorMapper.findByCollegeIdAndName(major.getCollegeId(), major.getName());
            if (existingMajor != null && !existingMajor.getId().equals(major.getId())) {
                return false;
            }
            return majorMapper.update(major) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteMajor(Long id) {
        try {
            return majorMapper.delete(id) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
