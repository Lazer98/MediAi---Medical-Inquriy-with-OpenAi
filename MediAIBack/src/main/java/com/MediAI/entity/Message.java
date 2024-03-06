package com.MediAI.entity;

import jakarta.persistence.Column;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

// Importing required classes
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "message")
// Class
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User1 sender_id;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User1 recipient_id;

    @Column(name="sender_name")
    private String sender_name;

    @Column(name="content")
    private String content;

    @Column(name="dateSent")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateSent ;

    public Message(User1 sender_id, User1 recipient_id, String sender_name, String content, LocalDate dateSent) {
        this.sender_id = sender_id;
        this.recipient_id = recipient_id;
        this.sender_name = sender_name;
        this.content = content;
        this.dateSent = dateSent;
    }

    public Message(User1 sender_id, String sender_name, String content, LocalDate dateSent) {
        this.sender_id = sender_id;
        this.sender_name = sender_name;
        this.content = content;
        this.dateSent = dateSent;
    }
}
