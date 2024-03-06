package com.MediAI.service;


// Importing packages
// Importing required classes

import com.MediAI.entity.Doctor;
import com.MediAI.entity.Inquiry;
import com.MediAI.entity.Patient;
import com.MediAI.repository.inquiryRepository.InquiryRepository;
import com.MediAI.repository.patientRepository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

// Class
@Service
public class InquiryServiceImpl implements InquiryService{

    @Autowired
    @Qualifier("InquiryRepository")
    private InquiryRepository inquiryRepository;

    @Autowired
    @Qualifier("PatientRepository")
    private PatientRepository patientRepository;

    // Read operation

    @Override
    public List<Inquiry> getAllInquiries() {
        return inquiryRepository.findAll();

    }


    @Override
    public List<Inquiry> getAllInquiriesByPatientId(Long patient_id) {
        //loop that runs over all inquiries and filters according to fitting user_id
        List<Inquiry> inquiryTempList= inquiryRepository.findAll();
        List<Inquiry> inquiryReturn= new ArrayList<Inquiry>();
        for(int i=0;i< inquiryTempList.toArray().length;i++){
            System.out.println(inquiryTempList.get(i));
            if(inquiryTempList.get(i).getPatient_id().getId()==patient_id){
                inquiryReturn.add(inquiryTempList.get(i));
            }
        }
        return inquiryReturn;
    }
    @Override
    public Inquiry getInquiryById(Long id) {
        Optional<Inquiry> optionalInquiry= inquiryRepository.findById(id);
        return optionalInquiry.orElse(null);
    }





    @Override
    public Inquiry createInquiry(Inquiry inquiry){

         return inquiryRepository.save(inquiry);

    }

    // Update operation

    @Override
    public Inquiry updateInquiry(Inquiry inquiry , Long inquiryId) {
        Inquiry inquiryDB = inquiryRepository.findById(inquiryId).orElse(null);

        if (inquiryDB == null) {
            throw new IllegalArgumentException("Inquiry not found");
        }


        if (Objects.nonNull(inquiry.getContentSendToAi()) && !"".equalsIgnoreCase(inquiry.getContentSendToAi())) {
            inquiryDB.setContentSendToAi(inquiry.getContentSendToAi());
        }

        if (Objects.nonNull(inquiry.getDiagnosisFromAi()) && !"".equalsIgnoreCase(inquiry.getDiagnosisFromAi())) {
            inquiryDB.setDiagnosisFromAi(inquiry.getDiagnosisFromAi());
        }


        return inquiryRepository.save(inquiryDB);
    }

    // Delete operation

    @Override
    public void deleteInquiryById(Long inquiryId)
    {
        Inquiry inquiryDB = inquiryRepository.findById(inquiryId).orElse(null);

        if (inquiryDB == null) {
            // Task not found, handle the error accordingly
            // You can throw an exception, return null, or customize the error message
            throw new IllegalArgumentException("Inquiry not found");
        }



        inquiryRepository.deleteById(inquiryId);
    }
}