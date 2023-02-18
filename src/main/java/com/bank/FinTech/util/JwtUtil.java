package com.bank.FinTech.util;

import com.bank.FinTech.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtUtil {
    private final UsersRepository usersRepository;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usersRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Not Found"));
    }

    public String loggedUserMail(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
