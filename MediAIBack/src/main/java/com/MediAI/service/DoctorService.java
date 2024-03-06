package com.MediAI.service;


// Importing packages
// Importing required classes
import com.MediAI.entity.Doctor;
import com.MediAI.entity.LoginRequest;
import com.MediAI.entity.Patient;
import com.MediAI.entity.User1;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

// Class
public interface DoctorService {



    // Read operation
    List<Doctor> getAllDoctors();

    Doctor getDoctorById(Long id);

    ResponseEntity<Doctor> getDoctorIdByEmail(@RequestBody LoginRequest loginRequest) ;

    ResponseEntity<Doctor> getDoctorByEmailLink(@RequestBody String email);


        Doctor createDoctor(Doctor doctor);

    // Update operation
    Doctor updateDoctor(Doctor doctor ,Long doctorId);

    // Delete operation
    void deleteDoctorById(Long doctorId);
}