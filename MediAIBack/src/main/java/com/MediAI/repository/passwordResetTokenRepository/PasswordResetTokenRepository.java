package com.MediAI.repository.passwordResetTokenRepository;

import com.MediAI.entity.PasswordResetToken;
import org.springframework.data.repository.CrudRepository;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long> {

    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUserEmail(String userEmail);
}
