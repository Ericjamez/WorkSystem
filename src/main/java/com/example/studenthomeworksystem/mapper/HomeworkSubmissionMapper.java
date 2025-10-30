package com.example.studenthomeworksystem.mapper;

import com.example.studenthomeworksystem.entity.HomeworkSubmission;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface HomeworkSubmissionMapper {
    
    @Select("SELECT hs.*, u.name as student_name, s.student_id as student_id_number, " +
            "h.title as homework_title, h.course_name " +
            "FROM homework_submissions hs " +
            "LEFT JOIN students s ON hs.student_id = s.id " +
            "LEFT JOIN users u ON s.id = u.id " +
            "LEFT JOIN homework h ON hs.homework_id = h.id")
    List<HomeworkSubmission> findAllWithDetails();
    
    @Select("SELECT hs.*, u.name as student_name, s.student_id as student_id_number, " +
            "h.title as homework_title, h.course_name " +
            "FROM homework_submissions hs " +
            "LEFT JOIN students s ON hs.student_id = s.id " +
            "LEFT JOIN users u ON s.id = u.id " +
            "LEFT JOIN homework h ON hs.homework_id = h.id " +
            "WHERE hs.id = #{id}")
    HomeworkSubmission findByIdWithDetails(Long id);
    
    @Select("SELECT hs.*, u.name as student_name, s.student_id as student_id_number, " +
            "h.title as homework_title, h.course_name, h.deadline as homework_deadline " +
            "FROM homework_submissions hs " +
            "LEFT JOIN students s ON hs.student_id = s.id " +
            "LEFT JOIN users u ON s.id = u.id " +
            "LEFT JOIN homework h ON hs.homework_id = h.id " +
            "WHERE hs.student_id = #{studentId}")
    List<HomeworkSubmission> findByStudentIdWithDetails(Long studentId);
    
    @Select("SELECT hs.*, u.name as student_name, s.student_id as student_id_number, " +
            "h.title as homework_title, h.course_name " +
            "FROM homework_submissions hs " +
            "LEFT JOIN students s ON hs.student_id = s.id " +
            "LEFT JOIN users u ON s.id = u.id " +
            "LEFT JOIN homework h ON hs.homework_id = h.id " +
            "WHERE hs.homework_id = #{homeworkId} AND hs.status != 'RETURNED'")
    List<HomeworkSubmission> findByHomeworkIdWithDetails(Long homeworkId);
    
    @Insert("INSERT INTO homework_submissions (student_id, homework_id, content, " +
            "attachment_path, score, teacher_comment, status) " +
            "VALUES (#{studentId}, #{homeworkId}, #{content}, #{attachmentPath}, " +
            "#{score}, #{teacherComment}, 'SUBMITTED')")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(HomeworkSubmission submission);
    
    @Update("UPDATE homework_submissions SET content = #{content}, " +
            "attachment_path = #{attachmentPath}, score = #{score}, " +
            "teacher_comment = #{teacherComment} WHERE id = #{id}")
    int update(HomeworkSubmission submission);
    
    @Update("UPDATE homework_submissions SET score = #{score}, " +
            "teacher_comment = #{teacherComment} WHERE id = #{id}")
    int gradeSubmission(@Param("id") Long id, @Param("score") Integer score, 
                       @Param("teacherComment") String teacherComment);
    
    @Delete("DELETE FROM homework_submissions WHERE id = #{id}")
    int delete(Long id);
    
    @Select("SELECT COUNT(*) FROM homework_submissions WHERE student_id = #{studentId} " +
            "AND homework_id = #{homeworkId}")
    int existsByStudentAndHomework(@Param("studentId") Long studentId, 
                                  @Param("homeworkId") Long homeworkId);
    
    @Update("UPDATE homework_submissions SET status = 'RETURNED', " +
            "return_reason = #{returnReason} WHERE id = #{id}")
    int returnSubmission(@Param("id") Long id, @Param("returnReason") String returnReason);
    
    @Update("UPDATE homework_submissions SET status = 'RESUBMITTED', " +
            "content = #{content}, attachment_path = #{attachmentPath}, " +
            "submit_time = CURRENT_TIMESTAMP WHERE id = #{id}")
    int resubmit(@Param("id") Long id, @Param("content") String content, 
                @Param("attachmentPath") String attachmentPath);
}
