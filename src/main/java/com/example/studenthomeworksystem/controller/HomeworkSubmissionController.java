package com.example.studenthomeworksystem.controller;

import com.example.studenthomeworksystem.entity.HomeworkSubmission;
import com.example.studenthomeworksystem.service.HomeworkSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/submissions")
public class HomeworkSubmissionController {
    
    @Autowired
    private HomeworkSubmissionService homeworkSubmissionService;
    
    @GetMapping
    public ResponseEntity<List<HomeworkSubmission>> getAllSubmissions() {
        List<HomeworkSubmission> submissions = homeworkSubmissionService.getAllSubmissionsWithDetails();
        return ResponseEntity.ok(submissions);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<HomeworkSubmission> getSubmissionById(@PathVariable Long id) {
        HomeworkSubmission submission = homeworkSubmissionService.getSubmissionByIdWithDetails(id);
        if (submission != null) {
            return ResponseEntity.ok(submission);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<HomeworkSubmission>> getSubmissionsByStudentId(@PathVariable Long studentId) {
        List<HomeworkSubmission> submissions = homeworkSubmissionService.getSubmissionsByStudentId(studentId);
        return ResponseEntity.ok(submissions);
    }
    
    @GetMapping("/homework/{homeworkId}")
    public ResponseEntity<List<HomeworkSubmission>> getSubmissionsByHomeworkId(@PathVariable Long homeworkId) {
        List<HomeworkSubmission> submissions = homeworkSubmissionService.getSubmissionsByHomeworkId(homeworkId);
        return ResponseEntity.ok(submissions);
    }
    
    @PostMapping
    public ResponseEntity<String> submitHomework(@RequestBody HomeworkSubmission submission) {
        boolean success = homeworkSubmissionService.submitHomework(submission);
        if (success) {
            return ResponseEntity.ok("作业提交成功");
        } else {
            return ResponseEntity.badRequest().body("作业提交失败，可能已经提交过该作业");
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<String> updateSubmission(@PathVariable Long id, @RequestBody HomeworkSubmission submission) {
        submission.setId(id);
        boolean success = homeworkSubmissionService.updateSubmission(submission);
        if (success) {
            return ResponseEntity.ok("作业提交更新成功");
        } else {
            return ResponseEntity.badRequest().body("作业提交更新失败");
        }
    }
    
    @PostMapping("/{id}/grade")
    public ResponseEntity<String> gradeSubmission(@PathVariable Long id, @RequestBody Map<String, Object> gradeData) {
        Integer score = (Integer) gradeData.get("score");
        String teacherComment = (String) gradeData.get("teacherComment");
        
        boolean success = homeworkSubmissionService.gradeSubmission(id, score, teacherComment);
        if (success) {
            return ResponseEntity.ok("作业评分成功");
        } else {
            return ResponseEntity.badRequest().body("作业评分失败");
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubmission(@PathVariable Long id) {
        boolean success = homeworkSubmissionService.deleteSubmission(id);
        if (success) {
            return ResponseEntity.ok("作业提交删除成功");
        } else {
            return ResponseEntity.badRequest().body("作业提交删除失败");
        }
    }
    
    @GetMapping("/check-submission")
    public ResponseEntity<Boolean> checkSubmission(@RequestParam Long studentId, @RequestParam Long homeworkId) {
        boolean hasSubmitted = homeworkSubmissionService.hasStudentSubmittedHomework(studentId, homeworkId);
        return ResponseEntity.ok(hasSubmitted);
    }
    
    @PostMapping("/{id}/return")
    public ResponseEntity<String> returnSubmission(@PathVariable Long id, @RequestBody Map<String, String> returnData) {
        String returnReason = returnData.get("returnReason");
        boolean success = homeworkSubmissionService.returnSubmission(id, returnReason);
        if (success) {
            return ResponseEntity.ok("作业打回成功");
        } else {
            return ResponseEntity.badRequest().body("作业打回失败");
        }
    }
    
    @PostMapping("/{id}/resubmit")
    public ResponseEntity<String> resubmitHomework(@PathVariable Long id, @RequestBody Map<String, String> resubmitData) {
        String content = resubmitData.get("content");
        String attachmentPath = resubmitData.get("attachmentPath");
        boolean success = homeworkSubmissionService.resubmitHomework(id, content, attachmentPath);
        if (success) {
            return ResponseEntity.ok("作业重新提交成功");
        } else {
            return ResponseEntity.badRequest().body("作业重新提交失败");
        }
    }
}
