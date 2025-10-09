package com.example.studenthomeworksystem.mapper;

import com.example.studenthomeworksystem.entity.Homework;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface HomeworkMapper {
    
    @Select("SELECT * FROM homework")
    List<Homework> findAll();
    
    @Select("SELECT * FROM homework WHERE id = #{id}")
    Homework findById(Long id);
    
    @Insert("INSERT INTO homework (title, description, course_name, deadline, total_score) " +
            "VALUES (#{title}, #{description}, #{courseName}, #{deadline}, #{totalScore})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Homework homework);
    
    @Update("UPDATE homework SET title = #{title}, description = #{description}, " +
            "course_name = #{courseName}, deadline = #{deadline}, total_score = #{totalScore} " +
            "WHERE id = #{id}")
    int update(Homework homework);
    
    @Delete("DELETE FROM homework WHERE id = #{id}")
    int delete(Long id);
    
    @Select("SELECT * FROM homework WHERE course_name = #{courseName}")
    List<Homework> findByCourseName(String courseName);
    
    @Select("SELECT * FROM homework WHERE deadline > NOW()")
    List<Homework> findActiveHomework();
}
