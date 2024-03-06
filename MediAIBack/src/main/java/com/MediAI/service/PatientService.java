package com.MediAI.service;

// Java Program to Illustrate TaskService.java File

// Importing packages
// Importing required classes
import com.MediAI.entity.Doctor;
import com.MediAI.entity.LoginRequest;
import com.MediAI.entity.Patient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

// Class
public interface PatientService {



    Patient createPatient(Patient patient);
    // Read operation
    List<Patient> getAllPatients();

    Patient getPatientById(Long id);

    ResponseEntity<Patient> getPatientIdByEmail(@RequestBody LoginRequest loginRequest);

    ResponseEntity<Patient> getPatientByEmailLink(@RequestBody String email);

    // Update operation
    Patient updatePatient(Patient patient, Long patientId ,Long doctorId);

    // Delete operation
    void deletePatientById(Long patientId);
}