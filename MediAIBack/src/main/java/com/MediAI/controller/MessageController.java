package com.MediAI.controller;

// Java Program to Illustrate userController.java File

// Importing packages modules



import com.MediAI.entity.CreateMessageRequest;
import com.MediAI.entity.LoginRequest;


import com.MediAI.entity.Message;
import com.MediAI.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;





    @GetMapping("")
    @CrossOrigin(origins = "*")
    public List<Message> getAllMessages() {

        return messageService.getAllMessages();
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
        Message message = messageService.getMessageById(id);
        if (message != null) {
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("")
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> createMessage(@RequestBody CreateMessageRequest request) {
        boolean saved = messageService.createMessage(
                request.getContent(),
                request.getSender_id(),
                request.getRecipient_id(),
                request.getSender_name(),
                request.getDateSent()
        );

        if (saved) {
            return ResponseEntity.ok("Message saved successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{messageId}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> updateMessage(@RequestBody  CreateMessageRequest request ,
                                                @PathVariable Long messageId) {
        boolean saved = messageService.updateMessage(
                request.getContent(),
                request.getSender_id(),
                request.getRecipient_id(),
                request.getSender_name(),
                request.getDateSent(),
                messageId
        );

        if (saved) {
            return ResponseEntity.ok("Message updated successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId) {
        messageService.deleteMessageById(messageId);
        return ResponseEntity.noContent().build();
    }
}
