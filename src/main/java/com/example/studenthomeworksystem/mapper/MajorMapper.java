package com.example.studenthomeworksystem.mapper;

import com.example.studenthomeworksystem.entity.Major;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MajorMapper {
    
    @Select("SELECT m.*, c.name as collegeName FROM majors m " +
            "LEFT JOIN colleges c ON m.college_id = c.id " +
            "ORDER BY m.create_time DESC")
    List<Major> findAll();
    
    @Select("SELECT m.*, c.name as collegeName FROM majors m " +
            "LEFT JOIN colleges c ON m.college_id = c.id " +
            "WHERE m.id = #{id}")
    Major findById(Long id);
    
    @Select("SELECT m.*, c.name as collegeName FROM majors m " +
            "LEFT JOIN colleges c ON m.college_id = c.id " +
            "WHERE m.college_id = #{collegeId} ORDER BY m.name")
    List<Major> findByCollegeId(Long collegeId);
    
    @Select("SELECT m.*, c.name as collegeName FROM majors m " +
            "LEFT JOIN colleges c ON m.college_id = c.id " +
            "WHERE m.college_id = #{collegeId} AND m.name = #{name}")
    Major findByCollegeIdAndName(@Param("collegeId") Long collegeId, @Param("name") String name);
    
    @Insert("INSERT INTO majors (college_id, name, description) VALUES (#{collegeId}, #{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Major major);
    
    @Update("UPDATE majors SET college_id = #{collegeId}, name = #{name}, description = #{description}, update_time = CURRENT_TIMESTAMP WHERE id = #{id}")
    int update(Major major);
    
    @Delete("DELETE FROM majors WHERE id = #{id}")
    int delete(Long id);
}
