package com.bank.FinTech.services.serviceImpl;

import com.bank.FinTech.dto.TransactionHistoryDto;
import com.bank.FinTech.exceptions.UserNotFoundException;
import com.bank.FinTech.models.Transaction;
import com.bank.FinTech.models.Users;
import com.bank.FinTech.models.Wallet;
import com.bank.FinTech.pagination_criteria.TransactionHistoryPages;
import com.bank.FinTech.repositories.TransactionRepository;
import com.bank.FinTech.repositories.UsersRepository;
import com.bank.FinTech.repositories.WalletRepository;
import com.bank.FinTech.services.TransactionHistory;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionHistoryImpl implements TransactionHistory {
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final UsersRepository usersRepository;

    public TransactionHistoryImpl(TransactionRepository transactionRepository, WalletRepository walletRepository, UsersRepository usersRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public PageImpl<TransactionHistoryDto> allTransaction(TransactionHistoryPages transactionHistoryPages) {

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = usersRepository.findUsersByEmail(userEmail).orElseThrow(()-> new UserNotFoundException("User not found"));
        Sort sort = Sort.by(transactionHistoryPages.getSortDirection(), transactionHistoryPages.getSortBy());
        Pageable pageable = PageRequest.of(transactionHistoryPages.getPageNumber(), transactionHistoryPages.getPageSize(), sort);


        Wallet wallet = walletRepository.findWalletByUsers(user);
        List<Transaction> allTransact = transactionRepository.findAllByWallet(wallet);

        String userAccountNumber = wallet.getAcctNumber();

        Page<Transaction> transactions = transactionRepository
                .findAllBySourceAccountNumberOrDestinationAccountNumber(userAccountNumber, userAccountNumber, pageable);


        List<TransactionHistoryDto> response = new ArrayList<>();
        for(Transaction transaction : allTransact){

            TransactionHistoryDto transactionHistoryDto = new TransactionHistoryDto();
            transactionHistoryDto.setId(transaction.getId());
            transactionHistoryDto.setName(transaction.getDestinationAccountName());
            transactionHistoryDto.setBank(transaction.getDestinationBank());
            transactionHistoryDto.setTransactionTime(transaction.getCreatedAt());
            transactionHistoryDto.setTransactionType(transaction.getTransactiontype());
            transactionHistoryDto.setAmount(transaction.getAmount());
            response.add(transactionHistoryDto);

        }
        PageImpl<TransactionHistoryDto> transactionHistoryPage = new PageImpl<>(response, pageable, transactions.getTotalElements());
        return transactionHistoryPage;
    }
    }
