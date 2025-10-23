package com.example.studenthomeworksystem.mapper;

import com.example.studenthomeworksystem.entity.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentMapper {
    
    @Select("SELECT s.*, u.name, u.email, u.phone, c.name as classNameFull, m.name as majorName, col.name as collegeName " +
            "FROM students s " +
            "LEFT JOIN users u ON s.id = u.id " +
            "LEFT JOIN classes c ON s.class_id = c.id " +
            "LEFT JOIN majors m ON c.major_id = m.id " +
            "LEFT JOIN colleges col ON m.college_id = col.id " +
            "ORDER BY s.student_id")
    List<Student> findAll();
    
    @Select("SELECT s.*, u.name, u.email, u.phone, c.name as classNameFull, m.name as majorName, col.name as collegeName " +
            "FROM students s " +
            "LEFT JOIN users u ON s.id = u.id " +
            "LEFT JOIN classes c ON s.class_id = c.id " +
            "LEFT JOIN majors m ON c.major_id = m.id " +
            "LEFT JOIN colleges col ON m.college_id = col.id " +
            "WHERE s.id = #{id}")
    Student findById(Long id);
    
    @Select("SELECT s.*, u.name, u.email, u.phone, c.name as classNameFull, m.name as majorName, col.name as collegeName " +
            "FROM students s " +
            "LEFT JOIN users u ON s.id = u.id " +
            "LEFT JOIN classes c ON s.class_id = c.id " +
            "LEFT JOIN majors m ON c.major_id = m.id " +
            "LEFT JOIN colleges col ON m.college_id = col.id " +
            "WHERE s.student_id = #{studentId}")
    Student findByStudentId(String studentId);
    
    @Insert("INSERT INTO students (id, student_id, class_name, major, class_id) VALUES (#{id}, #{studentId}, #{className}, #{major}, #{classId})")
    int insert(Student student);
    
    @Update("UPDATE students SET student_id = #{studentId}, class_name = #{className}, major = #{major}, class_id = #{classId} WHERE id = #{id}")
    int update(Student student);
    
    @Select("SELECT s.*, u.name, u.email, u.phone, c.name as classNameFull, m.name as majorName, col.name as collegeName " +
            "FROM students s " +
            "LEFT JOIN users u ON s.id = u.id " +
            "LEFT JOIN classes c ON s.class_id = c.id " +
            "LEFT JOIN majors m ON c.major_id = m.id " +
            "LEFT JOIN colleges col ON m.college_id = col.id " +
            "WHERE s.class_name = #{className} ORDER BY s.student_id")
    List<Student> findByClassName(String className);
    
    @Delete("DELETE FROM students WHERE id = #{id}")
    int delete(Long id);
}
