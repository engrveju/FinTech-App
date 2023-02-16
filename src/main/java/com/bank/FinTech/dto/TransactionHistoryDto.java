package com.bank.FinTech.dto;


import com.bank.FinTech.enums.Transactiontype;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Data
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionHistoryDto {
        private Long id;
        private String name;
        private String bank;
        private LocalDateTime transactionTime;
        private Transactiontype transactionType;
        private BigDecimal amount;
    }

