package com.bank.FinTech.services;

import com.bank.FinTech.dto.WalletDto;
import com.bank.FinTech.exceptions.UserNotFoundException;
import com.bank.FinTech.models.Wallet;
import com.bank.FinTech.request.FlwWalletRequest;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Service;


@Service
public interface WalletService {
    WalletDto viewWalletDetails() throws UserNotFoundException;

    Wallet createWallet(FlwWalletRequest walletRequest) throws JSONException;

}
