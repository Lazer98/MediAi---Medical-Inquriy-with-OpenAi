package com.MediAI.service;


// Importing packages
// Importing required classes

import com.MediAI.entity.Inquiry;

import java.time.LocalDate;
import java.util.List;

// Class
public interface InquiryService {



    // Read operation
    List<Inquiry> getAllInquiries();

    List<Inquiry> getAllInquiriesByPatientId(Long user_id);

    Inquiry getInquiryById(Long id);


    Inquiry createInquiry(Inquiry inquiry);

    // Update operation
    Inquiry updateInquiry(Inquiry inquiry , Long inquiryId);

    // Delete operation
    void deleteInquiryById(Long inquiryId);
}