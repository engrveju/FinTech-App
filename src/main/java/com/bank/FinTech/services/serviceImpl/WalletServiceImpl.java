package com.bank.FinTech.services.serviceImpl;

import com.bank.FinTech.dto.WalletDto;
import com.bank.FinTech.exceptions.UserNotFoundException;
import com.bank.FinTech.models.Users;
import com.bank.FinTech.models.Wallet;
import com.bank.FinTech.repositories.UsersRepository;
import com.bank.FinTech.repositories.WalletRepository;
import com.bank.FinTech.request.FlwWalletRequest;
import com.bank.FinTech.response.FlwVirtualAccountResponse;
import com.bank.FinTech.services.WalletService;
import com.bank.FinTech.util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;


@RequiredArgsConstructor
@Service
public class WalletServiceImpl implements WalletService {

private final WalletRepository walletRepository;
private final UsersRepository usersRepository;

    @Override
    public WalletDto viewWalletDetails() throws UserNotFoundException {
        WalletDto walletDto =new WalletDto();
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Users users = usersRepository.findByEmail(userEmail).
                orElseThrow(()-> new UserNotFoundException("Not found"));
        Wallet wallet = users.getWallet();
        walletDto.setBalance(wallet.getBalance());
        walletDto.setAcctNumber(wallet.getAcctNumber());
        BeanUtils.copyProperties(users, walletDto);
        return walletDto;
    }


    @Override
    public Wallet createWallet(FlwWalletRequest walletRequest) throws JSONException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + Constant.AUTHORIZATION);
        FlwWalletRequest payload = generatePayload(walletRequest);
        HttpEntity<FlwWalletRequest> request = new HttpEntity<>(payload, headers);

        FlwVirtualAccountResponse body = restTemplate.exchange(
                Constant.CREATE_VIRTUAL_ACCOUNT_API,
                HttpMethod.POST,
                request,
                FlwVirtualAccountResponse.class
        ).getBody();

        Wallet wallet = Wallet.builder()
                .bankName(body.getData().getBankName())
                .acctNumber(body.getData().getAccountNumber())
                .balance(Double.parseDouble(body.getData().getAmount()))
                .build();
        return wallet;
    }

    private FlwWalletRequest generatePayload(FlwWalletRequest flwWalletRequest){
        FlwWalletRequest jsonData = FlwWalletRequest.builder()
                .firstname(flwWalletRequest.getFirstname())
                .lastname(flwWalletRequest.getLastname())
                .email(flwWalletRequest.getEmail())
                .bvn(flwWalletRequest.getBvn())
                .is_permanent(true)
                .phonenumber(flwWalletRequest.getPhonenumber())
                .tx_ref("FinTech Payment App SQ-11A")
                .narration("Payment")
                .build();

            return jsonData;
    }
}
