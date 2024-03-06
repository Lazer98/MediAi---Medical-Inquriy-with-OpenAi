package com.MediAI.entity;

// Importing required package modules

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
@Table(name = "doctor")
// Class
public class Doctor extends User1 {



    @Column(name="docid")
    private String docid;




}