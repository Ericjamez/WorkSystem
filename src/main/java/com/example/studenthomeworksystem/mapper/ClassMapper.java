package com.example.studenthomeworksystem.mapper;

import com.example.studenthomeworksystem.entity.Class;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ClassMapper {
    
    @Select("SELECT c.*, m.name as majorName, col.name as collegeName, col.id as collegeId " +
            "FROM classes c " +
            "LEFT JOIN majors m ON c.major_id = m.id " +
            "LEFT JOIN colleges col ON m.college_id = col.id " +
            "ORDER BY c.create_time DESC")
    List<Class> findAll();
    
    @Select("SELECT c.*, m.name as majorName, col.name as collegeName, col.id as collegeId " +
            "FROM classes c " +
            "LEFT JOIN majors m ON c.major_id = m.id " +
            "LEFT JOIN colleges col ON m.college_id = col.id " +
            "WHERE c.id = #{id}")
    Class findById(Long id);
    
    @Select("SELECT c.*, m.name as majorName, col.name as collegeName, col.id as collegeId " +
            "FROM classes c " +
            "LEFT JOIN majors m ON c.major_id = m.id " +
            "LEFT JOIN colleges col ON m.college_id = col.id " +
            "WHERE c.major_id = #{majorId} ORDER BY c.name")
    List<Class> findByMajorId(Long majorId);
    
    @Select("SELECT c.*, m.name as majorName, col.name as collegeName, col.id as collegeId " +
            "FROM classes c " +
            "LEFT JOIN majors m ON c.major_id = m.id " +
            "LEFT JOIN colleges col ON m.college_id = col.id " +
            "WHERE c.major_id = #{majorId} AND c.name = #{name}")
    Class findByMajorIdAndName(@Param("majorId") Long majorId, @Param("name") String name);
    
    @Insert("INSERT INTO classes (major_id, name, description) VALUES (#{majorId}, #{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Class clazz);
    
    @Update("UPDATE classes SET major_id = #{majorId}, name = #{name}, description = #{description}, update_time = CURRENT_TIMESTAMP WHERE id = #{id}")
    int update(Class clazz);
    
    @Delete("DELETE FROM classes WHERE id = #{id}")
    int delete(Long id);
}
