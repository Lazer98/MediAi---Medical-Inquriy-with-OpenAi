package com.MediAI.repository.patientRepository;


import com.MediAI.entity.Patient;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository("PatientRepository")
public interface PatientRepository extends JpaRepository<Patient, Long> {

}
