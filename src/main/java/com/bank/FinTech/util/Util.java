package com.bank.FinTech.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Util {
    public boolean validatePassword(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }


}
