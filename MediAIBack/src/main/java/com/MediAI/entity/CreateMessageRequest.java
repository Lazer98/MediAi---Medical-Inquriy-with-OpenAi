package com.MediAI.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateMessageRequest {
    private String content;
    private long sender_id;
    private long recipient_id;
    private String sender_name;
    private LocalDate dateSent;

    // Add constructor, getters, and setters
}
