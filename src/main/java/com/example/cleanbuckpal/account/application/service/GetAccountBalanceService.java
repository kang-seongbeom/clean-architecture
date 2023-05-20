package com.example.cleanbuckpal.account.application.service;

import com.example.cleanbuckpal.account.application.port.in.GetAccountBalanceQuery;
import com.example.cleanbuckpal.account.application.port.out.LoadAccountPort;
import com.example.cleanbuckpal.account.domain.Account;
import com.example.cleanbuckpal.account.domain.Account.AccountId;
import com.example.cleanbuckpal.account.domain.Money;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class GetAccountBalanceService implements GetAccountBalanceQuery {

    private final LoadAccountPort loadAccountPort;

    @Override
    public Money getAccountBalance(AccountId accountId) {
        return loadAccountPort.loadAccount(accountId, LocalDateTime.now()).calculateBalance();
    }
}
