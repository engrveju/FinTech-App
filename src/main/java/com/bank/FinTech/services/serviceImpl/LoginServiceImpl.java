package com.bank.FinTech.services.serviceImpl;

import com.bank.FinTech.configuration.security.JwtService;
import com.bank.FinTech.dto.LoginRequestPayload;
import com.bank.FinTech.dto.MailServiceDto;
import com.bank.FinTech.exceptions.UserNotFoundException;
import com.bank.FinTech.models.Users;
import com.bank.FinTech.repositories.UsersRepository;
import com.bank.FinTech.request.ForgotPasswordRequest;
import com.bank.FinTech.request.PasswordRequest;
import com.bank.FinTech.services.LoginService;
import com.bank.FinTech.util.Constant;
import com.bank.FinTech.util.JwtUtil;
import com.bank.FinTech.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginServiceImpl implements LoginService {
    private final AuthenticationManager userAuthenticationManager;
    private final JwtService jwtService;
    private final UsersRepository usersRepository;
    private final MailServiceImpl mailService;
    private final Util utils;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String authenticate(LoginRequestPayload loginDto) throws Exception {
        loginDto.setEmail(loginDto.getEmail().toLowerCase());
        try {
            userAuthenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getEmail(),
                            loginDto.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new UserNotFoundException("Invalid Credentials");
        }
         UserDetails userDetails = jwtUtil.loadUserByUsername(loginDto.getEmail());
        return jwtService.generateToken(userDetails);
    }

    @Override
    public String generateResetToken(ForgotPasswordRequest forgotPasswordRequest) {
        String email = forgotPasswordRequest.getEmail();
        Users users = usersRepository.findUsersByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));


        String token = jwtService.generateToken(users);
        String link = Constant.RESET_PASSWORD_LINK + token;
        log.info("click here to reset your password" + link);
        sendPasswordResetEmail(users.getFirstName(), forgotPasswordRequest.getEmail(),link);

        return "Check your email to reset your password";
    }

    @Override
    public void sendPasswordResetEmail(String name, String email, String link) {
        String subject = "Reset your password";
        String body = "Please click the link below to reset your password";
        body += " " + link;

        MailServiceDto mailServiceDto = new MailServiceDto(name, email, body, subject);
        mailService.sendNotification(mailServiceDto);

    }

    @Override
    public String resetPassword(PasswordRequest passwordRequest, String token) {
        boolean passwordMatch = utils.validatePassword
                (passwordRequest.getNewPassword(), passwordRequest.getConfirmPassword());
        if(!passwordMatch){
            throw new InputMismatchException("Passwords do not match");
        }
        String email = jwtService.extractUsername(token);
        Users users = usersRepository.findUsersByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        users.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
        usersRepository.save(users);
        return "Password reset successful";
    }
}
