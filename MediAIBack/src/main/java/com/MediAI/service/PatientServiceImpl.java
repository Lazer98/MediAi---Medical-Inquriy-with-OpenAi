package com.MediAI.service;

// Java Program to Illustrate TaskServiceImpl.java
// File

// Importing required packages

// Importing required classes

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.MediAI.entity.Doctor;
;
import com.MediAI.entity.LoginRequest;
import com.MediAI.entity.Patient;
import com.MediAI.repository.doctorRepository.DoctorRepository;
import com.MediAI.repository.patientRepository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


// Annotation
@Service
// Class implementing TaskService class
public class PatientServiceImpl implements PatientService {

    @Autowired
    @Qualifier("PatientRepository")
    private PatientRepository patientRepository;

    @Autowired
    @Qualifier("DoctorRepository")
    private DoctorRepository doctorRepository;


    // Read operation
    @Override
    public List<Patient> getAllPatients() {
         return patientRepository.findAll();

    }

    @Override
    public Patient getPatientById(Long id){
        Optional<Patient> optionalPatient = patientRepository.findById(id);
        return optionalPatient.orElse(null);    }

    public ResponseEntity<Patient> getPatientIdByEmail(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPasswordOrDocId();

        // Retrieve all users from the database
        List<Patient> patients = getAllPatients();

        // Iterate over each user and check if the email and password match
        for (Patient patient : patients) {
            if (patient.getEmail().equals(email) && patient.getPassword().equals(password)) {
                return ResponseEntity.ok(patient);
            }
        }
        // No matching user found
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }

    public ResponseEntity<Patient> getPatientByEmailLink(@RequestBody String email) {
        // Retrieve all users from the database
        List<Patient> patients = getAllPatients();

        // Iterate over each user and check if the email and password match
        for (Patient patient : patients) {
            if (patient.getEmail().equals(email)) {
                return ResponseEntity.ok(patient);
            }
        }
        // No matching user found
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }

    //Post operation

    @Override
    public Patient createPatient(Patient patient){
        return patientRepository.save(patient);

    }
    // Update operation
    @Override
    public Patient updatePatient(Patient patient , Long patientId ,Long doctorId) {
        Patient patientDB = patientRepository.findById(patientId).orElse(null);
        Doctor doctorDB = doctorRepository.findById(doctorId).orElse(null);

        if (patientDB == null) {
            throw new IllegalArgumentException("Patient not found");
        }
        if (doctorDB == null) {
            throw new IllegalArgumentException("Doctor not found");
        }


        if (Objects.nonNull(patient.getName()) && !"".equalsIgnoreCase(patient.getName())) {
            patientDB.setName(patient.getName());
        }

        if (Objects.nonNull(patient.getEmail()) && !"".equalsIgnoreCase(patient.getEmail())) {
            patientDB.setEmail(patient.getEmail());
        }

        if (Objects.nonNull(patient.getPassword()) && !"".equalsIgnoreCase(patient.getPassword())) {
            patientDB.setEmail(patient.getPassword());
        }

        if (Objects.nonNull(patient.getBirthdate() )) {
            patientDB.setBirthdate(patient.getBirthdate());
        }

        if (Objects.nonNull(patient.getAge())) {
            patientDB.setAge(patient.getAge());
        }



        return patientRepository.save(patientDB);
    }

    // Delete operation
    @Override
    public void deletePatientById(Long patientId)
    {
        Patient depDB = patientRepository.findById(patientId).orElse(null);

        if (depDB == null) {
            // Task not found, handle the error accordingly
            // You can throw an exception, return null, or customize the error message
            throw new IllegalArgumentException("Patient not found");
        }



        patientRepository.deleteById(patientId);
    }
}