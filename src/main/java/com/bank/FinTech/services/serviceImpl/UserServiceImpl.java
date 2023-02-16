package com.bank.FinTech.services.serviceImpl;

import com.bank.FinTech.dto.RegistrationRequestDto;
import com.bank.FinTech.enums.UserStatus;
import com.bank.FinTech.exceptions.EmailAlreadyTakenException;
import com.bank.FinTech.exceptions.UserNotFoundException;
import com.bank.FinTech.models.Users;
import com.bank.FinTech.models.Wallet;
import com.bank.FinTech.repositories.ConfirmationTokenRepository;
import com.bank.FinTech.repositories.UsersRepository;
import com.bank.FinTech.repositories.WalletRepository;
import com.bank.FinTech.request.FlwWalletRequest;
import com.bank.FinTech.response.UserResponse;
import com.bank.FinTech.services.UsersService;
import com.bank.FinTech.services.WalletService;
import com.bank.FinTech.token.ConfirmationToken;
import com.bank.FinTech.util.JwtUtil;
import com.bank.FinTech.util.Util;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
@Builder
public class UserServiceImpl implements UserDetailsService, UsersService {
    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final WalletService walletService;
    private final ConfirmationTokenServiceImpl tokenService;
    private final WalletRepository walletRepository;
    private final Util util;
    private final JwtUtil jwtUtil;

    @Override
    public String registerUser(RegistrationRequestDto registrationRequestDto) throws JSONException {

        Users user = new Users();
        boolean userExists = usersRepository.findByEmail(registrationRequestDto.getEmail()).isPresent();
        boolean passwordMatch = util.validatePassword(registrationRequestDto.getPassword(),
                registrationRequestDto.getConfirmPassword());

        if (userExists) {
            throw new EmailAlreadyTakenException("Email Already Taken");
        }
        if (!passwordMatch) {
            throw new InputMismatchException("Passwords do not match!");
        }

        registrationRequestDto.setTransactionPin(passwordEncoder
                .encode(registrationRequestDto.getTransactionPin()));
        registrationRequestDto.setPassword(passwordEncoder.
                encode(registrationRequestDto.getPassword()));

        BeanUtils.copyProperties(registrationRequestDto, user);
        Users user1 = usersRepository.save(user);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(10),
                user
        );
        confirmationTokenRepository.save(confirmationToken);

        Wallet wallet = walletService.createWallet(FlwWalletRequest.builder()
                .firstname(user1.getFirstName())
                .lastname(user1.getLastName())
                .email(user1.getEmail())
                .phonenumber(user1.getPhoneNumber())
                .bvn(user1.getBvn())
                .build());
        wallet.setUsers(user1);
        wallet.setBalance(0.00);
        walletRepository.save(wallet);
        return token;
    }

    @Override
    public void enableUser(String email) {
        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));
        users.setEnabled(true);
        users.setUserStatus(UserStatus.ACTIVE);
        usersRepository.save(users);
    }

    @Override
    public void saveToken(String token, Users users) {
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(10),
                users
        );
        tokenService.saveConfirmationToken(confirmationToken);

    }

    @Override
    public UserResponse getUser() {
        String email = jwtUtil.loggedUserMail();
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(()-> new UserNotFoundException("User Not Found"));
        return UserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .bvn(user.getBvn())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UserNotFoundException {
        Optional<Users> users = usersRepository.findByEmail(email);
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("USER");
        if (users.isEmpty()) {
            throw new UserNotFoundException("Email not found in database");
        } else {
            return new User(users.get().getEmail(), users.get().getPassword(), Collections.singleton(authority));
        }

    }

}







