package com.MediAI.service;

// Java Program to Illustrate UserServiceImpl.java
// File

// Importing required packages




import com.MediAI.entity.Doctor;
import com.MediAI.entity.LoginRequest;
import com.MediAI.repository.doctorRepository.DoctorRepository;
import com.MediAI.repository.patientRepository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    @Qualifier("PatientRepository")
    private PatientRepository patientRepository;

    @Autowired
    @Qualifier("DoctorRepository")
    private DoctorRepository doctorRepository;


    // Read operation
    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();

    }

    @Override
    public Doctor getDoctorById(Long id){
        Optional<Doctor> optionalDoctor = doctorRepository.findById(id);
        return optionalDoctor.orElse(null);    }

    public ResponseEntity<Doctor> getDoctorIdByEmail(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String docid = loginRequest.getPasswordOrDocId();

        // Retrieve all users from the database
        List<Doctor> doctors = getAllDoctors();
        System.out.println(email + docid);
        // Iterate over each user and check if the email and password match
        for (Doctor doctor : doctors) {
            if (doctor.getEmail().equals(email) && doctor.getDocid().equals(docid)) {
                return ResponseEntity.ok(doctor);
            }
        }
        // No matching user found
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public ResponseEntity<Doctor> getDoctorByEmailLink(@RequestBody String email) {
        // Retrieve all users from the database
        List<Doctor> doctors = getAllDoctors();
        // Iterate over each user and check if the email and password match
        for (Doctor doctor : doctors) {
            if (doctor.getEmail().equals(email)) {
                return ResponseEntity.ok(doctor);
            }
        }
        // No matching user found
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    //Post operation

    @Override
    public Doctor createDoctor(Doctor doctor){
        return doctorRepository.save(doctor);

    }


    // Update operation
    @Override
    public Doctor updateDoctor(Doctor doctor  ,Long doctorId) {
        Doctor doctorDB = doctorRepository.findById(doctorId).orElse(null);

        if (doctorDB == null) {
            throw new IllegalArgumentException("Doctor not found");
        }


        if (Objects.nonNull(doctor.getName()) && !"".equalsIgnoreCase(doctor.getName())) {
            doctorDB.setName(doctor.getName());
        }

        if (Objects.nonNull(doctor.getEmail()) && !"".equalsIgnoreCase(doctor.getEmail())) {
            doctorDB.setEmail(doctor.getEmail());
        }

        if (Objects.nonNull(doctor.getDocid()) && !"".equalsIgnoreCase(doctor.getDocid())) {
            doctorDB.setDocid(doctor.getDocid());
        }

        return doctorRepository.save(doctorDB);
    }

    // Delete operation
    @Override
    public void deleteDoctorById(Long doctorId)
    {
        Doctor depDB = doctorRepository.findById(doctorId).orElse(null);

        if (depDB == null) {
            // Task not found, handle the error accordingly
            // You can throw an exception, return null, or customize the error message
            throw new IllegalArgumentException("Doctor not found");
        }



        doctorRepository.deleteById(doctorId);
    }
}
