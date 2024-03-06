package com.MediAI.controller;

// Java Program to Illustrate userController.java File

// Importing packages modules




import com.MediAI.entity.Inquiry;


import com.MediAI.entity.Patient;
import com.MediAI.repository.patientRepository.PatientRepository;
import com.MediAI.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inquiries")
public class InquiryController {
    @Autowired
    private InquiryService inquiryService;

    @Autowired
    @Qualifier("PatientRepository")
    private PatientRepository patientRepository;



    @GetMapping("")
    @CrossOrigin(origins = "*")
    public List<Inquiry> getAllInquiries() {

        return inquiryService.getAllInquiries();
    }

    @GetMapping("/user/{patient_id}")
    @CrossOrigin(origins = "*")
    public List<Inquiry> getAllInquiriesByPatientId(@PathVariable Long patient_id) {

        return inquiryService.getAllInquiriesByPatientId(patient_id);
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Inquiry> getInquiryById(@PathVariable Long id) {
        Inquiry inquiry = inquiryService.getInquiryById(id);
        if (inquiry != null) {
            return ResponseEntity.ok(inquiry);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/{patient_id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Inquiry> createInquiry(@RequestBody Inquiry inquiry  ,@PathVariable Long patient_id) {
        Patient patient = patientRepository.findById(patient_id).orElse(null);
        if (patient == null) {
            throw new IllegalArgumentException("Patient not found for this id");
        }
        inquiry.setPatient_id(patient);

        Inquiry createdInquiry = inquiryService.createInquiry(inquiry);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInquiry);
    }


    @PutMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Inquiry> updateInquiry(@RequestBody Inquiry updatedInquiry ,
                                                 @RequestBody Long inquiryId) {
        Inquiry inquiry = inquiryService.updateInquiry(updatedInquiry, inquiryId);
        if (inquiry != null) {
            return ResponseEntity.ok(inquiry);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Void> deleteInquiry(@PathVariable Long inquiryId) {
        inquiryService.deleteInquiryById(inquiryId);
        return ResponseEntity.noContent().build();
    }
}
