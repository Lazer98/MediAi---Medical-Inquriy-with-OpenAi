package com.MediAI.repository.doctorRepository;




import com.MediAI.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("DoctorRepository")
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    // additional methods if needed
}
