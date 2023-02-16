package com.bank.FinTech.services;

import com.bank.FinTech.models.FlwBank;
import com.bank.FinTech.models.Transaction;
import com.bank.FinTech.models.Users;
import com.bank.FinTech.request.ExternalBankTransferRequest;
import com.bank.FinTech.request.FlwResolveAccountRequest;
import com.bank.FinTech.response.FlwOtherBankTransferResponse;
import com.bank.FinTech.response.FlwResolveAccountDetails;

import java.math.BigDecimal;
import java.util.List;


public interface TransferService {
    List<FlwBank> getAllBanks();

    FlwResolveAccountDetails resolveAccount(FlwResolveAccountRequest resolveAccountRequest);

    Users retrieveUserDetails();

    boolean validatePin(String pin, Users users);

    boolean validateRequestBalance(BigDecimal requestAmount);

    boolean validateWalletBalance(BigDecimal requestAmount, Users users);

    FlwOtherBankTransferResponse initiateOtherBankTransfer(ExternalBankTransferRequest transferRequest);

    FlwOtherBankTransferResponse otherBankTransfer(ExternalBankTransferRequest transferRequest, String clientRef);

    Transaction saveTransactions (Users users, ExternalBankTransferRequest transferRequest);
}
