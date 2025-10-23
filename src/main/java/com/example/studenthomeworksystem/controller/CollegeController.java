package com.example.studenthomeworksystem.controller;

import com.example.studenthomeworksystem.entity.College;
import com.example.studenthomeworksystem.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/colleges")
public class CollegeController {

    @Autowired
    private CollegeService collegeService;

    @GetMapping
    public Map<String, Object> getAllColleges() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<College> colleges = collegeService.getAllColleges();
            result.put("success", true);
            result.put("colleges", colleges);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取学院列表失败");
        }
        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getCollegeById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            College college = collegeService.getCollegeById(id);
            if (college != null) {
                result.put("success", true);
                result.put("college", college);
            } else {
                result.put("success", false);
                result.put("message", "学院不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取学院信息失败");
        }
        return result;
    }

    @PostMapping
    public Map<String, Object> createCollege(@RequestBody College college) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (college.getName() == null || college.getName().trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "学院名称不能为空");
                return result;
            }

            boolean success = collegeService.createCollege(college);
            if (success) {
                result.put("success", true);
                result.put("message", "创建学院成功");
            } else {
                result.put("success", false);
                result.put("message", "学院名称已存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "创建学院失败");
        }
        return result;
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateCollege(@PathVariable Long id, @RequestBody College college) {
        Map<String, Object> result = new HashMap<>();
        try {
            college.setId(id);
            if (college.getName() == null || college.getName().trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "学院名称不能为空");
                return result;
            }

            boolean success = collegeService.updateCollege(college);
            if (success) {
                result.put("success", true);
                result.put("message", "更新学院成功");
            } else {
                result.put("success", false);
                result.put("message", "学院名称已存在或更新失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新学院失败");
        }
        return result;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteCollege(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = collegeService.deleteCollege(id);
            if (success) {
                result.put("success", true);
                result.put("message", "删除学院成功");
            } else {
                result.put("success", false);
                result.put("message", "删除学院失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除学院失败");
        }
        return result;
    }
}
