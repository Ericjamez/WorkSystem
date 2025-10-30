package com.example.studenthomeworksystem.service;

import com.example.studenthomeworksystem.entity.HomeworkSubmission;
import com.example.studenthomeworksystem.mapper.HomeworkSubmissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeworkSubmissionService {
    
    @Autowired
    private HomeworkSubmissionMapper homeworkSubmissionMapper;
    
    @Autowired
    private HomeworkService homeworkService;
    
    public List<HomeworkSubmission> getAllSubmissionsWithDetails() {
        return homeworkSubmissionMapper.findAllWithDetails();
    }
    
    public HomeworkSubmission getSubmissionByIdWithDetails(Long id) {
        return homeworkSubmissionMapper.findByIdWithDetails(id);
    }
    
    public List<HomeworkSubmission> getSubmissionsByStudentId(Long studentId) {
        return homeworkSubmissionMapper.findByStudentIdWithDetails(studentId);
    }
    
    public List<HomeworkSubmission> getSubmissionsByHomeworkId(Long homeworkId) {
        return homeworkSubmissionMapper.findByHomeworkIdWithDetails(homeworkId);
    }
    
    public boolean submitHomework(HomeworkSubmission submission) {
        try {
            // Check if student has already submitted this homework
            int exists = homeworkSubmissionMapper.existsByStudentAndHomework(
                submission.getStudentId(), submission.getHomeworkId());
            
            if (exists > 0) {
                return false; // Already submitted
            }
            
            homeworkSubmissionMapper.insert(submission);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateSubmission(HomeworkSubmission submission) {
        try {
            int result = homeworkSubmissionMapper.update(submission);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean gradeSubmission(Long submissionId, Integer score, String teacherComment) {
        try {
            int result = homeworkSubmissionMapper.gradeSubmission(submissionId, score, teacherComment);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteSubmission(Long id) {
        try {
            int result = homeworkSubmissionMapper.delete(id);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean hasStudentSubmittedHomework(Long studentId, Long homeworkId) {
        int count = homeworkSubmissionMapper.existsByStudentAndHomework(studentId, homeworkId);
        return count > 0;
    }
    
    public boolean returnSubmission(Long submissionId, String returnReason) {
        try {
            int result = homeworkSubmissionMapper.returnSubmission(submissionId, returnReason);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean resubmitHomework(Long submissionId, String content, String attachmentPath) {
        try {
            // 获取提交信息以检查作业截止时间
            HomeworkSubmission submission = homeworkSubmissionMapper.findByIdWithDetails(submissionId);
            if (submission == null) {
                return false;
            }
            
            // 检查作业是否已截止
            com.example.studenthomeworksystem.entity.Homework homework = homeworkService.getHomeworkById(submission.getHomeworkId());
            if (homework == null) {
                return false;
            }
            
            // 检查作业截止时间
            if (homework.getDeadline() != null && homework.getDeadline().isBefore(java.time.LocalDateTime.now())) {
                return false; // 作业已截止，不允许重新提交
            }
            
            int result = homeworkSubmissionMapper.resubmit(submissionId, content, attachmentPath);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
