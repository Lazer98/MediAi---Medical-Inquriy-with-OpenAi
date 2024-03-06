package com.MediAI.service;

import com.MediAI.entity.PasswordResetToken;
import com.MediAI.entity.Patient;
import com.MediAI.repository.passwordResetTokenRepository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    public void savePasswordResetToken(String userEmail, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setEmail(userEmail);
        passwordResetToken.setToken(token);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    public List<PasswordResetToken> getAllTokens() {
        return (List<PasswordResetToken>) passwordResetTokenRepository.findAll();

    }

    public PasswordResetToken getPasswordResetTokenByToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    public PasswordResetToken getPasswordResetTokenByUserEmail(String userEmail) {
        return passwordResetTokenRepository.findByUserEmail(userEmail);
    }
}
