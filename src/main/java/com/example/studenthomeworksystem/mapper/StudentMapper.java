package com.example.studenthomeworksystem.mapper;

import com.example.studenthomeworksystem.entity.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentMapper {
    
    @Select("SELECT * FROM students")
    List<Student> findAll();
    
    @Select("SELECT * FROM students WHERE id = #{id}")
    Student findById(Long id);
    
    @Select("SELECT * FROM students WHERE student_id = #{studentId}")
    Student findByStudentId(String studentId);
    
    @Insert("INSERT INTO students (student_id, name, class_name, email, phone) " +
            "VALUES (#{studentId}, #{name}, #{className}, #{email}, #{phone})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Student student);
    
    @Update("UPDATE students SET name = #{name}, class_name = #{className}, " +
            "email = #{email}, phone = #{phone} WHERE id = #{id}")
    int update(Student student);
    
    @Delete("DELETE FROM students WHERE id = #{id}")
    int delete(Long id);
    
    @Select("SELECT * FROM students WHERE class_name = #{className}")
    List<Student> findByClassName(String className);
}
