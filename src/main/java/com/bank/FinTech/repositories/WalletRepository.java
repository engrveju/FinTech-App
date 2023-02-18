package com.bank.FinTech.repositories;


import com.bank.FinTech.models.Users;
import com.bank.FinTech.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findWalletById(Long id);

    Wallet findWalletByUsers(Users user);
    Wallet findWalletByAcctNumber(String accountNumber);
    Optional<Wallet> findWalletByAcctNumberAndBankName(final String accountNumber, final String bankName);
    @Query(value = "SELECT * FROM wallet_tbl WHERE user_id = ?", nativeQuery = true)
    Wallet findWalletByUsersId(final Long Id);

}