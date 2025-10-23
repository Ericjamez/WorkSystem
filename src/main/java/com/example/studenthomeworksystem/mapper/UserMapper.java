package com.example.studenthomeworksystem.mapper;

import com.example.studenthomeworksystem.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    
    @Select("SELECT u.*, s.student_id, s.class_name, s.major, t.teacher_id, t.department " +
            "FROM users u " +
            "LEFT JOIN students s ON u.id = s.id " +
            "LEFT JOIN teachers t ON u.id = t.id " +
            "WHERE u.username = #{username}")
    User findByUsername(String username);
    
    @Select("SELECT u.*, s.student_id, s.class_name, s.major, t.teacher_id, t.department " +
            "FROM users u " +
            "LEFT JOIN students s ON u.id = s.id " +
            "LEFT JOIN teachers t ON u.id = t.id " +
            "WHERE s.student_id = #{studentId}")
    User findByStudentId(String studentId);
    
    @Select("SELECT u.*, s.student_id, s.class_name, s.major, t.teacher_id, t.department " +
            "FROM users u " +
            "LEFT JOIN students s ON u.id = s.id " +
            "LEFT JOIN teachers t ON u.id = t.id " +
            "WHERE t.teacher_id = #{teacherId}")
    User findByTeacherId(String teacherId);
    
    @Select("SELECT u.*, s.student_id, s.class_name, s.major, t.teacher_id, t.department " +
            "FROM users u " +
            "LEFT JOIN students s ON u.id = s.id " +
            "LEFT JOIN teachers t ON u.id = t.id " +
            "WHERE u.id = #{id}")
    User findById(Long id);
    
    @Select("SELECT u.*, s.student_id, s.class_name, s.major " +
            "FROM users u " +
            "JOIN students s ON u.id = s.id " +
            "WHERE u.role = 'STUDENT'")
    List<User> findAllStudents();
    
    @Select("SELECT u.*, t.teacher_id, t.department " +
            "FROM users u " +
            "JOIN teachers t ON u.id = t.id " +
            "WHERE u.role = 'TEACHER'")
    List<User> findAllTeachers();
    
    @Insert("INSERT INTO users (username, password, role, name, email, phone) " +
            "VALUES (#{username}, #{password}, #{role}, #{name}, #{email}, #{phone})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertUser(User user);
    
    @Insert("INSERT INTO students (id, student_id, class_name, major) " +
            "VALUES (#{id}, #{studentId}, #{className}, #{major})")
    int insertStudent(User user);
    
    @Insert("INSERT INTO teachers (id, teacher_id, department) " +
            "VALUES (#{id}, #{teacherId}, #{department})")
    int insertTeacher(User user);
}
