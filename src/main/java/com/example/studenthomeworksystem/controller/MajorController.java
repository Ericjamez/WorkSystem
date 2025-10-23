package com.example.studenthomeworksystem.controller;

import com.example.studenthomeworksystem.entity.Major;
import com.example.studenthomeworksystem.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/majors")
public class MajorController {

    @Autowired
    private MajorService majorService;

    @GetMapping
    public Map<String, Object> getAllMajors() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Major> majors = majorService.getAllMajors();
            result.put("success", true);
            result.put("majors", majors);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取专业列表失败");
        }
        return result;
    }

    @GetMapping("/college/{collegeId}")
    public Map<String, Object> getMajorsByCollegeId(@PathVariable Long collegeId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Major> majors = majorService.getMajorsByCollegeId(collegeId);
            result.put("success", true);
            result.put("majors", majors);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取专业列表失败");
        }
        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getMajorById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Major major = majorService.getMajorById(id);
            if (major != null) {
                result.put("success", true);
                result.put("major", major);
            } else {
                result.put("success", false);
                result.put("message", "专业不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取专业信息失败");
        }
        return result;
    }

    @PostMapping
    public Map<String, Object> createMajor(@RequestBody Major major) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (major.getName() == null || major.getName().trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "专业名称不能为空");
                return result;
            }
            if (major.getCollegeId() == null) {
                result.put("success", false);
                result.put("message", "学院ID不能为空");
                return result;
            }

            boolean success = majorService.createMajor(major);
            if (success) {
                result.put("success", true);
                result.put("message", "创建专业成功");
            } else {
                result.put("success", false);
                result.put("message", "专业名称已存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "创建专业失败");
        }
        return result;
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateMajor(@PathVariable Long id, @RequestBody Major major) {
        Map<String, Object> result = new HashMap<>();
        try {
            major.setId(id);
            if (major.getName() == null || major.getName().trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "专业名称不能为空");
                return result;
            }
            if (major.getCollegeId() == null) {
                result.put("success", false);
                result.put("message", "学院ID不能为空");
                return result;
            }

            boolean success = majorService.updateMajor(major);
            if (success) {
                result.put("success", true);
                result.put("message", "更新专业成功");
            } else {
                result.put("success", false);
                result.put("message", "专业名称已存在或更新失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新专业失败");
        }
        return result;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteMajor(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = majorService.deleteMajor(id);
            if (success) {
                result.put("success", true);
                result.put("message", "删除专业成功");
            } else {
                result.put("success", false);
                result.put("message", "删除专业失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除专业失败");
        }
        return result;
    }
}
