package com.bank.FinTech.controller;

import com.bank.FinTech.dto.LocalBankTransferDto;
import com.bank.FinTech.services.serviceImpl.LocalTransferServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/transfer")
public class LocalTransferController {
    private final LocalTransferServiceImpl localTransferService;

    @PutMapping("/localTransfer")
    public String localTransfer(@RequestBody LocalBankTransferDto localBankTransferDto){
        return localTransferService.localTransfer(localBankTransferDto);
    }
}
