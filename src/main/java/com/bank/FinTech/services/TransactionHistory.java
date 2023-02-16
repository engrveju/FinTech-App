package com.bank.FinTech.services;

import com.bank.FinTech.dto.TransactionHistoryDto;
import com.bank.FinTech.pagination_criteria.TransactionHistoryPages;
import org.springframework.data.domain.PageImpl;

public interface TransactionHistory {

    PageImpl<TransactionHistoryDto> allTransaction(TransactionHistoryPages transactionHistoryPages);

}
