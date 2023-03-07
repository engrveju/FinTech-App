package com.bank.FinTech.services.serviceImpl;


import com.bank.FinTech.dto.LocalBankTransferDto;
import com.bank.FinTech.enums.TransactionStatus;
import com.bank.FinTech.enums.Transactiontype;
import com.bank.FinTech.exceptions.IncorrectTransactionPinException;
import com.bank.FinTech.exceptions.InsufficientBalanceException;
import com.bank.FinTech.exceptions.InvalidAmountException;
import com.bank.FinTech.exceptions.WalletNotFoundException;
import com.bank.FinTech.models.Transaction;
import com.bank.FinTech.models.Users;
import com.bank.FinTech.models.Wallet;
import com.bank.FinTech.repositories.TransactionRepository;
import com.bank.FinTech.repositories.UsersRepository;
import com.bank.FinTech.repositories.WalletRepository;
import com.bank.FinTech.services.LocalTransferService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@AllArgsConstructor
@Builder
public class LocalTransferServiceImpl implements LocalTransferService {


    private final WalletRepository walletRepository;

    private final UsersRepository usersRepository;

    private final TransactionRepository transactionRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public String localTransfer(LocalBankTransferDto localBankTransferDto) {
        UUID uuid = UUID.randomUUID();
        String userEmail =  SecurityContextHolder.getContext().getAuthentication().getName();

        System.out.println(userEmail+" Logged usermail");
        Users loggedInUser = usersRepository.findUsersByEmail(userEmail).get();

        Wallet receiversWallet = walletRepository.findWalletByAcctNumberAndBankName(localBankTransferDto.getAccountNumber(), localBankTransferDto.getBankName()).orElse(null);
        Wallet senderWallet = walletRepository.findWalletByUsersId(loggedInUser.getId());



        if(localBankTransferDto.getAmount().doubleValue() <= 0){
            throw new InvalidAmountException("Invalid transfer amount!");
        }
        if(receiversWallet == null){
            throw new WalletNotFoundException("Invalid Account Number or Bank name!");
        }
        if(senderWallet.getBalance() < localBankTransferDto.getAmount().doubleValue()){
            throw new InsufficientBalanceException("Insufficient balance!");
        }
        if(!passwordEncoder.matches(localBankTransferDto.getPin(), loggedInUser.getTransactionPin())){
            throw new IncorrectTransactionPinException("Incorrect Transaction PIN");
        }

        Transaction senderTransaction = new Transaction();
        Transaction receiverTransaction = new Transaction();


        try {
            double senderBalance = walletRepository.findWalletByUsersId(usersRepository.findByEmail(userEmail).get().getId()).getBalance();
            double receiverBalance = walletRepository.findWalletByAcctNumber(receiversWallet.getAcctNumber()).getBalance();

            receiversWallet.setBalance(receiverBalance+localBankTransferDto.getAmount().doubleValue());
            senderWallet.setBalance(senderBalance-localBankTransferDto.getAmount().doubleValue());

            walletRepository.save(receiversWallet);
            walletRepository.save(senderWallet);
            senderTransaction.setTransactionStatus(TransactionStatus.SUCCESS);
        } catch (Exception e) {
            senderTransaction.setTransactionStatus(TransactionStatus.FAILED);
            throw new RuntimeException(e);
        }


        senderTransaction = Transaction.builder()
                .amount(localBankTransferDto.getAmount())
                .destinationAccountName(localBankTransferDto.getBankName())
                .narration(localBankTransferDto.getNarration())
                .destinationBank(receiversWallet.getBankName())
                .destinationAccountNumber(localBankTransferDto.getAccountNumber())
                .sourceAccountNumber(senderWallet.getAcctNumber())
                .transactiontype(Transactiontype.DEBIT)
                .transactionDate(localBankTransferDto.getTransactionDate())
                .transactionStatus(TransactionStatus.SUCCESS)
                .users(loggedInUser)
                .wallet(loggedInUser.getWallet())
                .sourceBank(senderWallet.getBankName())
                .clientRef(uuid.toString())
                .build();

        transactionRepository.save(senderTransaction);

        receiverTransaction = Transaction.builder()
                .amount(localBankTransferDto.getAmount())
                .destinationAccountName(localBankTransferDto.getBankName())
                .narration(localBankTransferDto.getNarration())
                .destinationBank(receiversWallet.getBankName())
                .destinationAccountNumber(localBankTransferDto.getAccountNumber())
                .sourceAccountNumber(senderWallet.getAcctNumber())
                .transactiontype(Transactiontype.CREDIT)
                .transactionDate(localBankTransferDto.getTransactionDate())
                .transactionStatus(TransactionStatus.SUCCESS)
                .users(receiversWallet.getUsers())
                .wallet(receiversWallet)
                .sourceBank(senderWallet.getBankName())
                .clientRef(uuid.toString())
                .build();

        transactionRepository.save(senderTransaction);
        transactionRepository.save(receiverTransaction);

        return "Transaction successful!";



    }
}
