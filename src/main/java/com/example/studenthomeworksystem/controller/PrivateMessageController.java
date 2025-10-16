package com.example.studenthomeworksystem.controller;

import com.example.studenthomeworksystem.entity.PrivateMessage;
import com.example.studenthomeworksystem.service.PrivateMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class PrivateMessageController {
    
    @Autowired
    private PrivateMessageService privateMessageService;
    
    @GetMapping("/user/{userId}")
    public Map<String, Object> getUserMessages(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<PrivateMessage> messages = privateMessageService.getUserMessages(userId);
            response.put("success", true);
            response.put("messages", messages);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取消息失败");
        }
        
        return response;
    }
    
    @GetMapping("/conversation/{userId1}/{userId2}")
    public Map<String, Object> getConversation(@PathVariable Long userId1, @PathVariable Long userId2) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<PrivateMessage> messages = privateMessageService.getConversation(userId1, userId2);
            response.put("success", true);
            response.put("messages", messages);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取对话失败");
        }
        
        return response;
    }
    
    @PostMapping("/send")
    public Map<String, Object> sendMessage(@RequestBody PrivateMessage message) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean success = privateMessageService.sendMessage(message);
            if (success) {
                response.put("success", true);
                response.put("message", "消息发送成功");
            } else {
                response.put("success", false);
                response.put("message", "消息发送失败");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "消息发送失败");
        }
        
        return response;
    }
    
    @PostMapping("/mark-read/{messageId}")
    public Map<String, Object> markAsRead(@PathVariable Long messageId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean success = privateMessageService.markAsRead(messageId);
            if (success) {
                response.put("success", true);
                response.put("message", "消息已标记为已读");
            } else {
                response.put("success", false);
                response.put("message", "标记失败");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "标记失败");
        }
        
        return response;
    }
    
    @GetMapping("/unread-count/{userId}")
    public Map<String, Object> getUnreadCount(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            int count = privateMessageService.getUnreadMessageCount(userId);
            response.put("success", true);
            response.put("count", count);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取未读消息数失败");
        }
        
        return response;
    }
}
