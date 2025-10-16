package com.example.studenthomeworksystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PrivateMessage {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String messageContent;
    private Boolean isRead;
    private LocalDateTime createTime;
    
    // Additional fields for display
    private String senderName;
    private String receiverName;
    private String senderRole;
    private String receiverRole;
}
