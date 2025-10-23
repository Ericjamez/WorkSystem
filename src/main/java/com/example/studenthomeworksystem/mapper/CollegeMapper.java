package com.example.studenthomeworksystem.mapper;

import com.example.studenthomeworksystem.entity.College;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CollegeMapper {
    
    @Select("SELECT * FROM colleges ORDER BY create_time DESC")
    List<College> findAll();
    
    @Select("SELECT * FROM colleges WHERE id = #{id}")
    College findById(Long id);
    
    @Select("SELECT * FROM colleges WHERE name = #{name}")
    College findByName(String name);
    
    @Insert("INSERT INTO colleges (name, description) VALUES (#{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(College college);
    
    @Update("UPDATE colleges SET name = #{name}, description = #{description}, update_time = CURRENT_TIMESTAMP WHERE id = #{id}")
    int update(College college);
    
    @Delete("DELETE FROM colleges WHERE id = #{id}")
    int delete(Long id);
}
