package com.example.studenthomeworksystem.mapper;

import com.example.studenthomeworksystem.entity.Homework;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface HomeworkMapper {
    
    @Select("SELECT h.*, u.name as teacher_name FROM homework h " +
            "JOIN teachers t ON h.teacher_id = t.id " +
            "JOIN users u ON t.id = u.id " +
            "ORDER BY h.create_time DESC")
    List<Homework> findAll();
    
    @Select("SELECT h.*, u.name as teacher_name FROM homework h " +
            "JOIN teachers t ON h.teacher_id = t.id " +
            "JOIN users u ON t.id = u.id " +
            "WHERE h.id = #{id}")
    Homework findById(Long id);
    
    @Insert("INSERT INTO homework (teacher_id, title, description, course_name, deadline, total_score) " +
            "VALUES (#{teacherId}, #{title}, #{description}, #{courseName}, #{deadline}, #{totalScore})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Homework homework);
    
    @Update("UPDATE homework SET title = #{title}, description = #{description}, " +
            "course_name = #{courseName}, deadline = #{deadline}, total_score = #{totalScore} " +
            "WHERE id = #{id}")
    int update(Homework homework);
    
    @Delete("DELETE FROM homework WHERE id = #{id}")
    int delete(Long id);
    
    @Select("SELECT h.*, u.name as teacher_name FROM homework h " +
            "JOIN teachers t ON h.teacher_id = t.id " +
            "JOIN users u ON t.id = u.id " +
            "WHERE h.course_name = #{courseName}")
    List<Homework> findByCourseName(String courseName);
    
    @Select("SELECT h.*, u.name as teacher_name FROM homework h " +
            "JOIN teachers t ON h.teacher_id = t.id " +
            "JOIN users u ON t.id = u.id " +
            "WHERE h.deadline > NOW()")
    List<Homework> findActiveHomework();
    
    @Select("SELECT h.*, u.name as teacher_name FROM homework h " +
            "JOIN teachers t ON h.teacher_id = t.id " +
            "JOIN users u ON t.id = u.id " +
            "WHERE h.teacher_id = #{teacherId} " +
            "ORDER BY h.create_time DESC")
    List<Homework> findByTeacherId(Long teacherId);
}
