package com.MediAI.controller;

// Java Program to Illustrate userController.java File

// Importing packages modules


import com.MediAI.entity.Doctor;
import com.MediAI.entity.LoginRequest;
import com.MediAI.entity.Patient;
import com.MediAI.entity.User1;

import com.MediAI.service.DoctorService;
import com.MediAI.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;





    @GetMapping("")
    @CrossOrigin(origins = "*")
    public List<Doctor> getAllDoctors() {

        return doctorService.getAllDoctors();
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorById(id);
        if (doctor != null) {
            return ResponseEntity.ok(doctor);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/email")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Doctor> getDoctorIdByEmail(@RequestBody LoginRequest loginRequest) {
        return doctorService.getDoctorIdByEmail(loginRequest);
    }

    @PostMapping("")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        Doctor createdDoctor = doctorService.createDoctor(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDoctor);
    }

    @PutMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Doctor> updateDoctor(@RequestBody Doctor updatedDoctor ,
                                                 @RequestBody Long doctorId) {
        Doctor doctor = doctorService.updateDoctor(updatedDoctor , doctorId);
        if (doctor != null) {
            return ResponseEntity.ok(doctor);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long doctorId) {
        doctorService.deleteDoctorById(doctorId);
        return ResponseEntity.noContent().build();
    }
}
