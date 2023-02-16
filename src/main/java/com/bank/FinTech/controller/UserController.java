package com.bank.FinTech.controller;


import com.bank.FinTech.dto.AuthResponse;
import com.bank.FinTech.dto.LoginRequestPayload;
import com.bank.FinTech.dto.RegistrationRequestDto;
import com.bank.FinTech.request.ForgotPasswordRequest;
import com.bank.FinTech.request.PasswordRequest;
import com.bank.FinTech.response.UserResponse;
import com.bank.FinTech.services.LoginService;
import com.bank.FinTech.services.UsersService;
import com.bank.FinTech.services.serviceImpl.RegistrationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UsersService usersService;
    private final LoginService loginService;
    private final RegistrationServiceImpl registrationService;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequestPayload loginRequestPayload) throws Exception {
        String token = loginService.authenticate(loginRequestPayload);
        AuthResponse authResponse = new AuthResponse(token);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
    @PostMapping("/registration")
    public ResponseEntity<String> createUserAccount(@Valid @RequestBody RegistrationRequestDto
                                                            requestDto) throws JSONException {
        return new ResponseEntity<> (registrationService.createUser(requestDto), HttpStatus.CREATED);
    }

    @GetMapping( "/confirm")
    public ResponseEntity<String> confirmToken(@RequestParam("token") String token ){
        return new ResponseEntity<>(registrationService.confirmToken(token), HttpStatus.OK);
    }



    @GetMapping("/getUser")
    public ResponseEntity<UserResponse> getUser(){
        UserResponse userResponse = usersService.getUser();
        return new ResponseEntity<>(userResponse,HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest){
        return new ResponseEntity<>(loginService.generateResetToken(forgotPasswordRequest), HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody PasswordRequest passwordRequest, @RequestParam("token") String token){
        return loginService.resetPassword(passwordRequest, token);
    }
}
