package com.bank.FinTech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletDto {

    private String firstName;
    private String lastName;
    private Double balance;
    private String acctNumber;
    private String bankName = "Wema Bank";
}
