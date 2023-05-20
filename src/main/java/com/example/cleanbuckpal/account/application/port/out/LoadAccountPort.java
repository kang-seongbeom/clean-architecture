package com.example.cleanbuckpal.account.application.port.out;

import com.example.cleanbuckpal.account.domain.Account;
import com.example.cleanbuckpal.account.domain.Account.AccountId;

import java.time.LocalDateTime;

public interface LoadAccountPort {

    Account loadAccount(AccountId accountId, LocalDateTime baselineDate);
}
