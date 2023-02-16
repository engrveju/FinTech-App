package com.bank.FinTech.services;

import com.bank.FinTech.dto.RegistrationRequestDto;
import com.bank.FinTech.models.Users;
import org.springframework.boot.configurationprocessor.json.JSONException;

public interface RegistrationService {

    String createUser(RegistrationRequestDto registrationRequestDto) throws JSONException;

    String confirmToken(String token);

    void resendEmail(Users users);

    void sendMail(String name, String email, String link);
}
