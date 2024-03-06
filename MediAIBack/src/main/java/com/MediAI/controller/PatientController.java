package com.MediAI.controller;

// Java Program to Illustrate userController.java File

// Importing packages modules


import com.MediAI.entity.Doctor;
import com.MediAI.entity.LoginRequest;
import com.MediAI.entity.Patient;
import com.MediAI.entity.User1;

import com.MediAI.repository.patientRepository.PatientRepository;
import com.MediAI.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {
    @Autowired
    private PatientService patientService;
    @Autowired
    private PatientRepository patientRepository;


    @GetMapping("")
    @CrossOrigin(origins = "*")
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Patient patient = patientService.getPatientById(id);
        if (patient != null) {
            return ResponseEntity.ok(patient);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/email")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Patient> getPatientIdByEmail(@RequestBody LoginRequest loginRequest) {
        return patientService.getPatientIdByEmail(loginRequest);
    }


    @PostMapping("")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient createdUser = patientService.createPatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long patientId, @RequestBody Patient updatedPatient ,
                                                 @RequestBody Long doctorId) {
        Patient patient = patientService.updatePatient(updatedPatient ,patientId, doctorId);
        if (patient != null) {
            return ResponseEntity.ok(patient);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Void> deletePatient(@PathVariable Long patientId) {
        patientService.deletePatientById(patientId);
        return ResponseEntity.noContent().build();
    }
}
