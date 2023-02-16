package com.bank.FinTech.response;

import com.bank.FinTech.models.FlwBank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlwGetAllBanksResponse {

    private String status;
    private String message;
    private List<FlwBank> data = new ArrayList<>();

}
