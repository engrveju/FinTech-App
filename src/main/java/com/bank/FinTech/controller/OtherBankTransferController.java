package com.bank.FinTech.controller;

import com.bank.FinTech.models.FlwBank;
import com.bank.FinTech.request.ExternalBankTransferRequest;
import com.bank.FinTech.request.FlwResolveAccountRequest;
import com.bank.FinTech.response.FlwOtherBankTransferResponse;
import com.bank.FinTech.response.FlwResolveAccountDetails;
import com.bank.FinTech.services.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transfer")
@RequiredArgsConstructor
public class OtherBankTransferController {

    private final TransferService transferService;

    @GetMapping("/banks")
    public List<FlwBank> getBanks(){

        return transferService.getAllBanks();
    }

    @PostMapping("/otherbank-account-query")
    public ResponseEntity<FlwResolveAccountDetails> resolveAccountDetails(@RequestBody FlwResolveAccountRequest accountRequest){
        return new ResponseEntity<>(transferService.resolveAccount(accountRequest), HttpStatus.OK);
    }

    @PostMapping ("/other-bank")
    public ResponseEntity<FlwOtherBankTransferResponse> processTransfer(@RequestBody ExternalBankTransferRequest transferRequest){
        return new ResponseEntity<>(transferService.initiateOtherBankTransfer(transferRequest), HttpStatus.OK);
    }
}
