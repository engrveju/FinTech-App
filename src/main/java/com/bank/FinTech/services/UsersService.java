package com.bank.FinTech.services;

import com.bank.FinTech.dto.RegistrationRequestDto;
import com.bank.FinTech.models.Users;
import com.bank.FinTech.response.UserResponse;
import org.springframework.boot.configurationprocessor.json.JSONException;

public interface UsersService {

    String registerUser(RegistrationRequestDto registrationRequestDto) throws JSONException;
    void enableUser(String email);
    void saveToken(String token, Users users);
    UserResponse getUser();
}
