package com.bank.FinTech.models;

import com.bank.FinTech.enums.TransactionStatus;
import com.bank.FinTech.enums.Transactiontype;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "transaction_tbl")
public class Transaction extends BaseClass{


    private String sourceAccountNumber;

    private String sourceBank;

    @Column(length = 10, nullable = false)
    private String destinationAccountNumber;

    private String destinationAccountName;

    @Column(nullable = false)
    private String destinationBank;

    @Enumerated(EnumType.STRING)
    private Transactiontype transactiontype;

    @CreationTimestamp
    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Column(nullable = false)
    private BigDecimal amount;

    private String narration;

    private String clientRef;

    private String flwRef;

    @ManyToOne
    @JoinColumn(name = "wallet_id", referencedColumnName = "id")
    private Wallet wallet;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users users;

}
