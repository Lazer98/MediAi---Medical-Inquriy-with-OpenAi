package com.MediAI.service;


// Importing packages
// Importing required classes

import com.MediAI.entity.Message;

import java.time.LocalDate;
import java.util.List;

// Class
public interface MessageService {



    // Read operation
    List<Message> getAllMessages();

    Message getMessageById(Long id);

    List<Message> getAllMessagesOfUser(Long id);

    boolean createMessage(String content, long sender_id, long recipient_id ,String sender_name, LocalDate dateSent);

    // Update operation
    boolean updateMessage(String content, long sender_id, long recipient_id ,String sender_name, LocalDate dateSent ,Long messageId);

    // Delete operation
    void deleteMessageById(Long messageId);
}