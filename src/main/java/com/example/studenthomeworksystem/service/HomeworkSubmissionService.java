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
}
