package com.example.studenthomeworksystem.controller;

import com.example.studenthomeworksystem.entity.Class;
import com.example.studenthomeworksystem.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/classes")
public class ClassController {

    @Autowired
    private ClassService classService;

    @GetMapping
    public Map<String, Object> getAllClasses() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Class> classes = classService.getAllClasses();
            result.put("success", true);
            result.put("classes", classes);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取班级列表失败");
        }
        return result;
    }

    @GetMapping("/major/{majorId}")
    public Map<String, Object> getClassesByMajorId(@PathVariable Long majorId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Class> classes = classService.getClassesByMajorId(majorId);
            result.put("success", true);
            result.put("classes", classes);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取班级列表失败");
        }
        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getClassById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Class clazz = classService.getClassById(id);
            if (clazz != null) {
                result.put("success", true);
                result.put("class", clazz);
            } else {
                result.put("success", false);
                result.put("message", "班级不存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取班级信息失败");
        }
        return result;
    }

    @PostMapping
    public Map<String, Object> createClass(@RequestBody Class clazz) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (clazz.getName() == null || clazz.getName().trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "班级名称不能为空");
                return result;
            }
            if (clazz.getMajorId() == null) {
                result.put("success", false);
                result.put("message", "专业ID不能为空");
                return result;
            }

            boolean success = classService.createClass(clazz);
            if (success) {
                result.put("success", true);
                result.put("message", "创建班级成功");
            } else {
                result.put("success", false);
                result.put("message", "班级名称已存在");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "创建班级失败");
        }
        return result;
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateClass(@PathVariable Long id, @RequestBody Class clazz) {
        Map<String, Object> result = new HashMap<>();
        try {
            clazz.setId(id);
            if (clazz.getName() == null || clazz.getName().trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "班级名称不能为空");
                return result;
            }
            if (clazz.getMajorId() == null) {
                result.put("success", false);
                result.put("message", "专业ID不能为空");
                return result;
            }

            boolean success = classService.updateClass(clazz);
            if (success) {
                result.put("success", true);
                result.put("message", "更新班级成功");
            } else {
                result.put("success", false);
                result.put("message", "班级名称已存在或更新失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新班级失败");
        }
        return result;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteClass(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = classService.deleteClass(id);
            if (success) {
                result.put("success", true);
                result.put("message", "删除班级成功");
            } else {
                result.put("success", false);
                result.put("message", "删除班级失败");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除班级失败");
        }
        return result;
    }
}
