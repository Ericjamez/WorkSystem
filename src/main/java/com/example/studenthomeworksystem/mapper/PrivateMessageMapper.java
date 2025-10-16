package com.example.studenthomeworksystem.mapper;

import com.example.studenthomeworksystem.entity.PrivateMessage;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PrivateMessageMapper {
    
    @Select("SELECT pm.*, sender.name as sender_name, sender.role as sender_role, " +
            "receiver.name as receiver_name, receiver.role as receiver_role " +
            "FROM private_messages pm " +
            "JOIN users sender ON pm.sender_id = sender.id " +
            "JOIN users receiver ON pm.receiver_id = receiver.id " +
            "WHERE pm.sender_id = #{userId} OR pm.receiver_id = #{userId} " +
            "ORDER BY pm.create_time DESC")
    List<PrivateMessage> findByUserId(Long userId);
    
    @Select("SELECT pm.*, sender.name as sender_name, sender.role as sender_role, " +
            "receiver.name as receiver_name, receiver.role as receiver_role " +
            "FROM private_messages pm " +
            "JOIN users sender ON pm.sender_id = sender.id " +
            "JOIN users receiver ON pm.receiver_id = receiver.id " +
            "WHERE (pm.sender_id = #{userId1} AND pm.receiver_id = #{userId2}) " +
            "OR (pm.sender_id = #{userId2} AND pm.receiver_id = #{userId1}) " +
            "ORDER BY pm.create_time ASC")
    List<PrivateMessage> findConversation(Long userId1, Long userId2);
    
    @Insert("INSERT INTO private_messages (sender_id, receiver_id, message_content) " +
            "VALUES (#{senderId}, #{receiverId}, #{messageContent})")
    int insert(PrivateMessage message);
    
    @Update("UPDATE private_messages SET is_read = TRUE WHERE id = #{id}")
    int markAsRead(Long id);
    
    @Select("SELECT COUNT(*) FROM private_messages WHERE receiver_id = #{userId} AND is_read = FALSE")
    int countUnreadMessages(Long userId);
}
