package com.MediAI.repository.inquiryRepository;

import com.MediAI.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("InquiryRepository")
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
}
