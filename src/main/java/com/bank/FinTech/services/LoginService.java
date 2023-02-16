package com.bank.FinTech.services;

import com.bank.FinTech.dto.LoginRequestPayload;
import com.bank.FinTech.request.ForgotPasswordRequest;
import com.bank.FinTech.request.PasswordRequest;

public interface LoginService {
    String authenticate(LoginRequestPayload loginDto) throws Exception;
    String generateResetToken(ForgotPasswordRequest forgotPasswordRequest);
    void sendPasswordResetEmail(String name, String email, String link);
    String resetPassword(PasswordRequest passwordRequest, String token);
}
