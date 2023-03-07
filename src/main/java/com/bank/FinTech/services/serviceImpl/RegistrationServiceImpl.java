package com.bank.FinTech.services.serviceImpl;

import com.bank.FinTech.dto.MailServiceDto;
import com.bank.FinTech.dto.RegistrationRequestDto;
import com.bank.FinTech.exceptions.ConfirmationTokenException;
import com.bank.FinTech.exceptions.EmailNotValidException;
import com.bank.FinTech.exceptions.TokenNotFoundException;
import com.bank.FinTech.exceptions.UserNotFoundException;
import com.bank.FinTech.models.Users;
import com.bank.FinTech.repositories.UsersRepository;
import com.bank.FinTech.services.RegistrationService;
import com.bank.FinTech.token.ConfirmationToken;
import com.bank.FinTech.util.Constant;
import com.bank.FinTech.validations.EmailValidator;
import lombok.AllArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@AllArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final UserServiceImpl userService;
    private final EmailValidator emailValidator;
    private final MailServiceImpl mailService;
    private final UsersRepository usersRepository;
    private final ConfirmationTokenServiceImpl confirmationTokenService;

    @Override
    public String createUser(RegistrationRequestDto registrationRequestDto) throws JSONException {
        registrationRequestDto.setEmail(registrationRequestDto.getEmail().toLowerCase());
        boolean isValidEmail = emailValidator.test(registrationRequestDto.getEmail());
        if(!isValidEmail) {
            throw new EmailNotValidException("Email Not Valid");
        }
        String token = userService.registerUser(registrationRequestDto);

        if(token.equalsIgnoreCase("Bvn already Exists")){
            return "BVN ALREADY EXISTS";
        }

        String link = Constant.EMAIL_VERIFICATION_TOKEN_LINK + token;
         sendMail(registrationRequestDto.getFirstName().toLowerCase(),
                 registrationRequestDto.getEmail(), link);
        return "Please check your email to verify your account";
    }

    @Override
    @Transactional
    public String confirmToken(String token) {

        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(()-> new TokenNotFoundException("Token Not Found"));

        if (confirmationToken.getConfirmedAt() != null){
            throw new ConfirmationTokenException("Email Already Confirmed");
        }
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
           Users users = usersRepository.findByEmail(confirmationToken.getUsers().getEmail())
                   .orElseThrow(()-> new UserNotFoundException("User Not Found"));
           resendEmail(users);
           return "Verification token expired. Check email for a new verification token";
        }
        confirmationTokenService.setConfirmedAt(token);
        userService.enableUser(
                confirmationToken.getUsers().getEmail());
        return "Confirmed";
    }

    @Override
    public void resendEmail(Users users) {
        String token = UUID.randomUUID().toString();
        String link = Constant.EMAIL_VERIFICATION_TOKEN_LINK + token;
        sendMail(users.getFirstName(),users.getEmail(), link);

        userService.saveToken(token, users);
    }

    @Override
    public void sendMail(String name, String email, String link){

        String subject = "Email Verification";
        String body = "Click the link below to verify your email \n " + link;
        MailServiceDto mailServiceDto = new MailServiceDto(name, email, body, subject);
        mailService.sendNotification(mailServiceDto);

    }


}
