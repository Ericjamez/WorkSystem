package com.example.studenthomeworksystem.service;

import com.example.studenthomeworksystem.entity.PrivateMessage;
import com.example.studenthomeworksystem.mapper.PrivateMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrivateMessageService {
    
    @Autowired
    private PrivateMessageMapper privateMessageMapper;
    
    public List<PrivateMessage> getUserMessages(Long userId) {
        return privateMessageMapper.findByUserId(userId);
    }
    
    public List<PrivateMessage> getConversation(Long userId1, Long userId2) {
        return privateMessageMapper.findConversation(userId1, userId2);
    }
    
    public boolean sendMessage(PrivateMessage message) {
        try {
            privateMessageMapper.insert(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean markAsRead(Long messageId) {
        try {
            privateMessageMapper.markAsRead(messageId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public int getUnreadMessageCount(Long userId) {
        return privateMessageMapper.countUnreadMessages(userId);
    }
}
