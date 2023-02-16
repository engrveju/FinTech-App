package com.bank.FinTech.response;
import com.bank.FinTech.dto.TransactionHistoryDto;
import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewTransactionHistoryResponse {

    private Page<TransactionHistoryDto> transactions;
    }

