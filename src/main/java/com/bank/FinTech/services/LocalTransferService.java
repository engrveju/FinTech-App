package com.bank.FinTech.services;

import com.bank.FinTech.dto.LocalBankTransferDto;

public interface LocalTransferService {

    public String localTransfer(final LocalBankTransferDto localBankTransferDto);
}
