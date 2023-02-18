package com.bank.FinTech.controller;

import com.bank.FinTech.dto.WalletDto;
import com.bank.FinTech.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;
    @GetMapping("/viewWallet")
    public ResponseEntity<WalletDto> viewWallet() {
        WalletDto walletDto = walletService.viewWalletDetails();
        return new ResponseEntity<>(walletDto, HttpStatus.OK);
    }

}
