package com.bank.FinTech.services;

import com.bank.FinTech.token.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenService {
    void saveConfirmationToken(ConfirmationToken token);
    Optional<ConfirmationToken> getToken(String token);
    int setConfirmedAt(String token);
}
