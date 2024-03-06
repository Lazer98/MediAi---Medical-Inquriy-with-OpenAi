package com.MediAI.controller;

import com.MediAI.entity.Doctor;
import com.MediAI.entity.LoginRequest;
import com.MediAI.entity.Patient;
import com.MediAI.entity.TokenGenerator;
import com.MediAI.repository.doctorRepository.DoctorRepository;
import com.MediAI.repository.patientRepository.PatientRepository;
import com.MediAI.service.DoctorService;
import com.MediAI.service.PasswordResetService;
import com.MediAI.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    public PasswordResetController() {
    }

    //this is called when the user clicks on "password forgot" and enters his email adress
    @PostMapping("/forgot-password")
    public ResponseEntity<String> initiatePasswordReset(@RequestBody LoginRequest loginRequest) {
        ResponseEntity<Doctor> optionalDoctor = doctorService.getDoctorIdByEmail(loginRequest);
        ResponseEntity<Patient> optionalPatient = patientService.getPatientIdByEmail(loginRequest);



        // Generate the token
        String token = TokenGenerator.generateToken();
        // Store the token along with the user's email in the table
        if(!optionalDoctor.equals(null) || !optionalPatient.equals(null)){
            passwordResetService.savePasswordResetToken(loginRequest.getEmail(), token);

        }
        //now the token and the email are saved in the database


        // Send the email with the password reset link containing the generated token
        // (Implementation of email sending is not included in this example)

        return ResponseEntity.ok("Password reset initiated. Please check your email.");
    }


    //this is called when the user gets an email and clicks
    // on the link to update his password -> token is returned
    //now the token sent has to be read in the front code and the following data has to be translated from the token:
    // password (in order to login and check if the user is authorized)

    @PostMapping("/new-password/{email}")
    public ResponseEntity<Optional> getTheToken(@PathVariable String email) {
        ResponseEntity<Doctor> optionalDoctor = doctorService.getDoctorByEmailLink(email);
        ResponseEntity<Patient> optionalPatient = patientService.getPatientByEmailLink(email);
        Optional foundToken = Optional.ofNullable("not found");


        // Store the token along with the user's email in the table
        if (!optionalDoctor.equals(null) || !optionalPatient.equals(null)) {
            foundToken = Optional.ofNullable(passwordResetService.getAllTokens()
                    .stream()
                    .filter(token -> token.getEmail().equals(email)).findFirst());
        }
        if (!foundToken.isPresent()) {
            return ResponseEntity.notFound().build();

        } else {
            return ResponseEntity.ok(foundToken);
        }
    }
}
