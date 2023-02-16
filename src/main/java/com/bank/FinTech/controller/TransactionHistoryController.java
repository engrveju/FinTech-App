package com.bank.FinTech.controller;

import com.bank.FinTech.dto.TransactionHistoryDto;
import com.bank.FinTech.pagination_criteria.TransactionHistoryPages;
import com.bank.FinTech.services.TransactionHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TransactionHistoryController {
    private final TransactionHistory transactionHistory;
    @GetMapping("/transactionHistory")
    public ResponseEntity<PageImpl<TransactionHistoryDto>> viewTransactionHistory(TransactionHistoryPages transactionHistoryPages) {
        return ResponseEntity.ok(transactionHistory.allTransaction(transactionHistoryPages));
    }
    }
