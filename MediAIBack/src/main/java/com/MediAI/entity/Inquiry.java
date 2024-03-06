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
@Table(name = "inquiry")
// Class
public class Inquiry {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name="contentSendToAi")
    private String contentSendToAi;

    @Column(name="diagnosisFromAi")
    private String diagnosisFromAi;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient_id;

    @Column(name="dateSent")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateSent ;
}
