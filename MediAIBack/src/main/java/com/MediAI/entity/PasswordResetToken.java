package com.MediAI.entity;


// Importing required package modules

// Importing required classes
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
// Class
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Getter
    @Setter
    @Column(name = "email", unique = true)
    private String email;

    @Getter
    @Setter
    @Column(name = "token", unique = true)
    private String token;


}
